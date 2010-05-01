(ns seesaw.core
  (:use [seesaw listeners spectator util component-utils])
  (:use [clojure.contrib swing-utils logging])
  (:import [javax.swing JCheckBox JTextField JFrame JLabel JRadioButton ButtonGroup]))

(defn make-component-observer [key get-state set-state component]
  (fn [old new]
    (when (not= (key new) (get-state component))
      (set-state component (key new)))))

(defn update-from-event!
  [_ context key value-fn]
  (update! context {key (value-fn)}))

(defmacro defwatch
  ([name get-state set-state]
     `(defwatch ~name ~get-state ~set-state (fn [& _#])))
  ([name get-state set-state add-listener]
      `(defn ~name [component# context# key#]
	 (apply ~add-listener [component# context# key#])
	 (update! context# {key# (~get-state component#)} true)
	 (let [observer# (make-component-observer key# ~get-state ~set-state component#)]
	   (add-observer! context# observer# key#))
	 component#)))

;;;;;;;;;;;;;;;;;;;; Components with watches ;;;;;;;;;;;;;;;;;;;;

(defwatch watch-checkbox
  checkbox-selected?
  select-checkbox
  (fn [component context key]
    (add-change-listener component update-from-event!
			 context key #(checkbox-selected? component))))

(defn checkbox 
  ([context key]
     (watch-checkbox (JCheckBox.) context key))
  ([context key arg0]
     (watch-checkbox (JCheckBox. arg0) context key))
  ([context key arg0 arg1]
     (watch-checkbox (JCheckBox. arg0 arg1) context key))
  ([context key arg0 arg1 arg2]
     (watch-checkbox (JCheckBox. arg0 arg1 arg2) context key)))

(defwatch watch-text-field
  text-field-value
  set-text-field
  (fn [component context key]
    (add-document-listener (.getDocument component)
			   {:changed update-from-event!
			    :insert update-from-event!
			    :delete update-from-event!}
			   context key #(text-field-value component))))

(defn text-field
  ([context key]
     (watch-text-field (JTextField.) context key))
  ([context key arg0]
     (watch-text-field (JTextField. arg0) context key))
  ([context key arg0 arg1]
     (watch-text-field (JTextField. arg0 arg1) context key))
  ([context key arg0 arg1 arg2]
     (watch-text-field (JTextField. arg0 arg1 arg2) context key)))

(defn radio-button
  ([key]
     (set-action-command (JRadioButton.) (keyword-str key))))

(defwatch watch-button-group
  button-group-value
  set-button-group)

(defn button-group
  ([context key & buttons]
     (let [group (ButtonGroup.)]
       (doseq [button buttons]
	 (.add group button)
	 (add-change-listener
	  button
	  (fn [evt]
	    (let [button-value {(keyword (.getActionCommand button))
				(radio-button-selected? button)}
		  group-value (merge (key @context) button-value)]
	      (update! context {key group-value})))))
       (watch-button-group group context key))))

;;;;;;;;;;;;;;;;;;;; Components with no watches ;;;;;;;;;;;;;;;;;;;;

(defn frame
  ([]
     (JFrame.))
  ([arg0]
     (JFrame. arg0))
  ([arg0 arg1]
     (JFrame. arg0 arg1)))

(defn label
  ([]
     (JLabel.))
  ([arg0]
     (JLabel. arg0))
  ([arg0 arg1]
     (JLabel. arg0 arg1))
  ([arg0 arg1 arg2]
     (JLabel. arg0 arg1 arg2)))