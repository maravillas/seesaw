(ns seesaw.component-utils
  (:use [seesaw utils]))

(defn checkbox-selected?
  [checkbox]
  (.isSelected checkbox))

(defn select-checkbox
  [checkbox selected]
  (.setSelected checkbox selected))

(defn text-field-value
  [text-field]
  (.getText text-field))

(defn set-text-field
  [text-field text]
  (.setText text-field text))

(defn button-selected?
  [button]
  (.isSelected button))

(defn select-button
  [button selected]
  (.setSelected button selected))

(defn add-to-group
  [button button-group]
  (.add button-group button)
  button)

(defn set-action-command
  [button command]
  (.setActionCommand button command)
  button)

(defn button-group-buttons
  [button-group]
  (enumeration-seq (.getElements button-group)))

(defn find-button
  [button-group key]
  (let [action-command (name key)]
    (some #(and (= (.getActionCommand %1) action-command) %1)
	  (button-group-buttons button-group))))

(defn button-group-value
  [group]
  (apply hash-map (mapcat (fn [button] [(keyword (.getActionCommand button))
					(button-selected? button)])
			  (enumeration-seq (.getElements group)))))

(defn set-button-group
  [button-group new-value]
  (doseq [key (keys new-value)]
    (let [button (find-button button-group key)
	  model (.getModel button)
	  value (key new-value)]
      (.setSelected button-group model value))))

(defn listbox-values
  [listbox]
  (.. listbox getModel getElements ))

(defn set-listbox-values
  [listbox values]
  (.. listbox getModel (setElements values)))

(defn listbox-selection
  [listbox]
  (seq (.getSelectedValues listbox)))

(defn set-listbox-selection
  [listbox values]
  (doseq [value values]
    (.setSelectedValue listbox value false)))

(defn text-pane-value
  [text-pane]
  (.getText text-pane))

(defn set-text-pane
  [text-pane text]
  (.setText text-pane text))