(ns seesaw.core
  (:use [seesaw listeners spectator])
  (:use [clojure.contrib swing-utils logging])
  (:import [javax.swing JCheckBox JTextField JFrame JLabel]))

(defn make-component-observer [key get-state set-state component]
  (fn [old new]
    (when (not= (key new) (get-state component))
      (set-state component (key new)))))

(defn update-from-event!
  [_ context key value-fn]
  (update! context {key (value-fn)}))

(defmacro defwatch [name get-state set-state add-listener]
  `(defn ~name [component# context# key#]
     (apply ~add-listener [component# context# key#])
     (update! context# {key# (~get-state component#)} true)
     (let [observer# (make-component-observer key# ~get-state ~set-state component#)]
       (add-observer! context# observer# key#))
     component#))

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