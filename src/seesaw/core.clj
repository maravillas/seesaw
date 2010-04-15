(ns seesaw.core
  (:use [seesaw listeners spectator])
  (:use [clojure.contrib swing-utils logging])
  (:import [javax.swing JCheckBox JFrame]))

(defn make-component-observer [key get-state set-state component]
  (fn [old new]
    (when (not= (key new) (get-state component))
      (set-state component (key new)))))

(defn update-from-event!
  [_ context key value-fn]
  (update! context {key (value-fn)}))

;;;;;;;;;;;;;;;;;;;; Components with watches ;;;;;;;;;;;;;;;;;;;;

(defn- checkbox-selected? [checkbox]
  (let [model (.. checkbox getModel)]
    (.isSelected model)))

(defn- select-checkbox [checkbox selected]
  (let [model (.. checkbox getModel)]
    (.setSelected checkbox selected)))

(defn watch-checkbox [component context key]
  (add-change-listener component update-from-event! context key #(checkbox-selected? component))
  (update! context {key (checkbox-selected? component)} true)
  (let [observer (make-component-observer key checkbox-selected? select-checkbox component)]
    (add-observer! context observer key))
  component)

(defn checkbox 
  ([context key]
     (watch-checkbox (JCheckBox.) context key))
  ([context key arg0]
     (watch-checkbox (JCheckBox. arg0) context key))
  ([context key arg0 arg1]
     (watch-checkbox (JCheckBox. arg0 arg1) context key))
  ([context key arg0 arg1 arg2]
     (watch-checkbox (JCheckBox. arg0 arg1 arg2) context key)))

(defn frame
  ([]
     (JFrame.))
  ([arg0]
     (JFrame. arg0))
  ([arg0 arg1]
     (JFrame. arg0 arg1)))