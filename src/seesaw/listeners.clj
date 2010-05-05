(ns seesaw.listeners
  (:import [javax.swing.event ChangeListener DocumentListener ListDataListener ListSelectionListener]))

(defn- nop [& args])

(defn add-change-listener
  "Adds a ChangeListener to component. When the event fires, f will be 
  invoked with the event as its first argument followed by args.

  Fired when the target of the listener has changed its state.

  Returns the listener."
  [component f & args]
  (let [listener (proxy [ChangeListener] []
		     (stateChanged [evt] (apply f evt args)))]
    (.addChangeListener component listener)
    listener))

(defn add-document-listener
  "Adds a DocumentListener to component. When an event fires, the 
  corresponding function will be invoked with the event as its first 
  argument followed by args.

  Events:
    changed - An attribute or set of attributes changed.
    insert  - There was an insert into the document.
    remove  - A portion of the document has been removed.

  Returns the listener."
  [component {:keys [changed insert remove]} & args]
  (let [changed-fn (or changed nop)
	insert-fn (or insert nop)
	remove-fn (or remove nop)
	listener (proxy [DocumentListener] []
		   (changedUpdate [evt] (apply changed-fn evt args))
		   (insertUpdate  [evt] (apply insert-fn evt args))
		   (removeUpdate  [evt] (apply remove-fn evt args)))]
    (.addDocumentListener component listener)
    listener))

(defn add-list-data-listener
  "Adds a ListDataListener to component. When an event fires, the 
  corresponding function will be invoked with the event as its first 
  argument followed by args.

  Events:
    contents-changed - The contents of the list has changed in a way that's too
                       complex to characterize with the other events.
    interval-added   - The indices in the event's interval have been inserted
                       into the data model.
    interval-removed - The indices in the event's interval have been removed
                       from the data model. 

  Returns the listener."
  [component {:keys [contents-changed interval-added interval-removed]} & args]
  (let [contents-changed-fn (or contents-changed nop)
	interval-added-fn (or interval-added nop)
	interval-removed-fn (or interval-removed nop)
	listener (proxy [ListDataListener] []
		   (contentsChanged [evt] (apply contents-changed-fn evt args))
		   (intervalAdded   [evt] (apply interval-added-fn evt args))
		   (intervalRemoved [evt] (apply interval-removed-fn evt args)))]
    (.addListDataListener component listener)
    listener))

(defn add-list-selection-listener
  "Adds a ListSelectionListener to component. When the event fires, f will be 
  invoked with the event as its first argument followed by args.

  Fired when the value of the selection changes.

  Returns the listener."
  [component f & args]
  (let [listener (proxy [ListSelectionListener] []
		   (valueChanged [evt] (apply f evt args)))]
    (.addListSelectionListener component listener)
    listener))