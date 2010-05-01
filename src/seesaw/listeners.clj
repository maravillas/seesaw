(ns seesaw.listeners
  (:import [javax.swing.event ChangeListener DocumentListener]))

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