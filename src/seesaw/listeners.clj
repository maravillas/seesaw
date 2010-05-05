(ns seesaw.listeners
  (:import [javax.swing.event ChangeListener DocumentListener ListDataListener ListSelectionListener]))

(defn- nop [& args])

(defn add-change-listener [component f & args]
  "Adds a ChangeListener to component. When the event fires, f will be 
invoked with the event as its first argument followed by args.
Returns the listener."
  (let [listener (proxy [ChangeListener] []
		     (stateChanged [evt] (apply f evt args)))]
    (.addChangeListener component listener)
    listener))

(defn add-document-listener [component {:keys [changed insert remove]} & args]
  "Adds a DocumentListener to component. When an event fires, the 
corresponding function will be invoked with the event as its first 
argument followed by args. Returns the listener."
  (let [changed-fn (or changed nop)
	insert-fn (or insert nop)
	remove-fn (or remove nop)
	listener (proxy [DocumentListener] []
		   (changedUpdate [evt] (apply changed-fn evt args))
		   (insertUpdate  [evt] (apply insert-fn evt args))
		   (removeUpdate  [evt] (apply remove-fn evt args)))]
    (.addDocumentListener component listener)
    listener))

(defn add-list-data-listener [component {:keys [contents-changed
						interval-added
						interval-removed]}
			      & args]
  (let [contents-changed-fn (or contents-changed nop)
	interval-added-fn (or interval-added nop)
	interval-removed-fn (or interval-removed nop)
	listener (proxy [ListDataListener] []
		   (contentsChanged [evt] (apply contents-changed-fn evt args))
		   (intervalAdded   [evt] (apply interval-added-fn evt args))
		   (intervalRemoved [evt] (apply interval-removed-fn evt args)))]
    (.addListDataListener component listener)
    listener))

(defn add-list-selection-listener [component f & args]
  (let [listener (proxy [ListSelectionListener] []
		   (valueChanged [evt] (apply f evt args)))]
    (.addListSelectionListener component listener)
    listener))