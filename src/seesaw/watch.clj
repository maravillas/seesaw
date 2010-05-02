(ns seesaw.watch
  (:use [seesaw spectator listeners component-utils]))

(defn- make-component-observer [key get-state set-state component]
  (fn [old new]
    (when (not= (key new) (get-state component))
      (set-state component (key new)))))

(defn- update-from-event!
  [_ context key value-fn]
  (update! context {key (value-fn)}))

(defn- watch-component
  ([get-fn set-fn]
     (watch-component get-fn set-fn (fn [& _])))
  ([get-fn set-fn add-listeners-fn]
      (fn [component context key]
	(add-listeners-fn component context key)
	(update! context {key (get-fn component)} true)
	(let [observer (make-component-observer key get-fn set-fn component)]
	  (add-observer! context observer key)))))

(defn- add-checkbox-listeners
  [component context key]
  (add-change-listener component update-from-event!
		       context key #(checkbox-selected? component)))

(defn watch-checkbox
  [component context key]
  (let [watch (watch-component checkbox-selected?
			      select-checkbox
			      add-checkbox-listeners)]
    (watch component context key)))

(defn- add-text-field-listeners
  [component context key]
    (add-document-listener (.getDocument component)
			   {:changed update-from-event!
			    :insert update-from-event!
			    :delete update-from-event!}
			   context key #(text-field-value component)))

(defn watch-text-field
  [component context key]
  (let [watch (watch-component text-field-value
			       set-text-field
			       add-text-field-listeners)]
    (watch component context key)))

(defn watch-button-group
  [component context key value-fn]
  (let [watch (watch-component
	       (partial button-group-value value-fn)
	       set-button-group)]
    (watch component context key)))
