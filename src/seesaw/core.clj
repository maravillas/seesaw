(ns seesaw.core
  (:use [seesaw listeners spectator])
  (:use [clojure.contrib swing-utils logging])
  (:import [javax.swing JCheckBox JTextField JFrame]))

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

(defn- checkbox-selected? [checkbox]
  (.. checkbox getModel isSelected))

(defn- select-checkbox [checkbox selected]
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

(defn- textfield-value [textfield]
  (.getText textfield))

(defn- set-textfield [textfield text]
  (.setText textfield text))

(defwatch watch-textfield
  textfield-value
  set-textfield
  (fn [component context key]
    (add-document-listener (.getDocument component)
			   {:changed update-from-event!
			    :insert update-from-event!
			    :delete update-from-event!}
			   context key #(textfield-value component))))

(defn watch-textfield [component context key]
  (add-document-listener (.getDocument component)
			 {:changed update-from-event!
			  :insert update-from-event!
			  :delete update-from-event!}
			 context key #(textfield-value component))
  (update! context {key (textfield-value component)} true)
  (let [observer (make-component-observer key textfield-value set-textfield component)]
    (add-observer! context observer key))
  component)

(defn textfield
  ([context key]
     (watch-textfield (JTextField.) context key))
  ([context key arg0]
     (watch-textfield (JTextField. arg0) context key))
  ([context key arg0 arg1]
     (watch-textfield (JTextField. arg0 arg1) context key))
  ([context key arg0 arg1 arg2]
     (watch-textfield (JTextField. arg0 arg1 arg2) context key)))

(defn frame
  ([]
     (JFrame.))
  ([arg0]
     (JFrame. arg0))
  ([arg0 arg1]
     (JFrame. arg0 arg1)))