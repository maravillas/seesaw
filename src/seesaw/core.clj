(ns seesaw.core
  (:use [seesaw listeners spectator util])
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

(defn checkbox-selected? [checkbox]
  (.. checkbox getModel isSelected))

(defn select-checkbox [checkbox selected]
  (.setSelected checkbox selected))

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

(defn text-field-value [text-field]
  (.getText text-field))

(defn set-text-field [text-field text]
  (.setText text-field text))

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

(defn radio-button-selected? [radio-button]
  (.isSelected radio-button))

(defn select-radio-button [radio-button selected]
  (.setSelected radio-button selected))

(defn- add-to-group [button button-group]
  (.add button-group button)
  button)

(defn- set-action-command [button command]
  (.setActionCommand button command)
  button)

(defn radio-button
  ([key]
     (set-action-command (JRadioButton.) (keyword-str key))))

(defn button-group-buttons [button-group]
  (enumeration-seq (.getElements button-group)))

(defn button-in-group [button-group key]
  (some #(and (= (.getActionCommand %1) key) %1) (button-group-buttons button-group)))

(defn button-group-value [button-group]
  (apply hash-map (mapcat (fn [button] [(keyword (.getActionCommand button))
					(radio-button-selected? button)])
			  (enumeration-seq (.getElements button-group)))))

(defn set-button-group [button-group new-value]
  (doseq [key (keys new-value)]
    (let [button (button-in-group button-group key)
	  model (.getModel button)
	  value (key new-value)]
      (.setSelected button-group model value))))

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