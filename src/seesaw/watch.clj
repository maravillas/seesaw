(ns seesaw.watch
  (:use [seesaw spectator listeners component-utils]))

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


(defwatch watch-checkbox
  checkbox-selected?
  select-checkbox
  (fn [component context key]
    (add-change-listener component update-from-event!
			 context key #(checkbox-selected? component))))

(defwatch watch-text-field
  text-field-value
  set-text-field
  (fn [component context key]
    (add-document-listener (.getDocument component)
			   {:changed update-from-event!
			    :insert update-from-event!
			    :delete update-from-event!}
			   context key #(text-field-value component))))

(defwatch watch-button-group
  button-group-value
  set-button-group)