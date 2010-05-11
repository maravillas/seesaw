(ns seesaw.watch
  (:use [seesaw spectator listeners component-utils])
  (:use [clojure.contrib.logging]))

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
			   context key #(text-value component)))

(defn watch-text-field
  [component context key]
  (let [watch (watch-component text-value
			       set-text
			       add-text-field-listeners)]
    (watch component context key)))

(defn- add-button-listeners
  [component context key]
  (add-change-listener component update-from-event!
		       context key #(button-selected? component)))

(defn watch-button
  [component context key]
  (let [watch (watch-component button-selected?
			       select-button
			       add-button-listeners)]
    (watch component context key)))

(defn watch-button-group
  [component context key]
  (let [watch (watch-component button-group-value
			       set-button-group)]
    (watch component context key)))

(defn- add-listbox-value-listener
  [component context key]
  (add-list-data-listener (.getModel component)
			  {:contents-changed update-from-event!
			   :interval-added update-from-event!
			   :interval-removed update-from-event!}
			  context key #(listbox-values component)))

(defn- add-listbox-selection-listener
  [component context key]
  (add-list-selection-listener component update-from-event!
			       context key #(listbox-selection component)))

(defn watch-listbox
  [component context selection-key values-key]
  (let [watch-value (watch-component listbox-values
				     set-listbox-values
				     add-listbox-value-listener)
	watch-selection (watch-component listbox-selection
					 set-listbox-selection
					 add-listbox-selection-listener)]
    (watch-value component context values-key)
    (watch-selection component context selection-key)))

(defn- add-text-pane-listener
  [component context key]
  (add-document-listener (.getDocument component)
			 {:changed update-from-event!
			  :insert update-from-event!
			  :remove update-from-event!}
			 context key #(text-value component)))

(defn watch-text-pane
  [component context key]
  (let [watch (watch-component text-value
			       set-text
			       add-text-pane-listener)]
    (watch component context key)))