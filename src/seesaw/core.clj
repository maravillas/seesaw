(ns seesaw.core
  (:use [seesaw listeners spectator watch component-utils utils])
  (:import [seesaw.models SettableListModel]))

(defn set-properties
  [component properties-values]
  (doseq [[prop val] (partition 2 properties-values)]
    (let [val (if (coll? val) val [val])]
      (apply str-invoke (keyword-to-setter prop) component val))))

;;;;;;;;;;;;;;;;;;;; Components with watches ;;;;;;;;;;;;;;;;;;;;

(defn- abstract-button
  [button context key & options]
  (doto button
    (watch-button context key)
    (set-properties options)
    (set-action-command (name key))))

(defn checkbox 
  [context key & options]
  (apply abstract-button (javax.swing.JCheckBox.) context key options))

(defn radio-button
  [context key & options]
  (apply abstract-button (javax.swing.JRadioButton.) context key options))

(defn toggle-button
  [context key & options]
  (apply abstract-button (javax.swing.JToggleButton.) context key options))

(defn button-group
  [context key & buttons]
  (let [group (javax.swing.ButtonGroup.)]
    (doseq [button buttons]
      (.add group button)
      (add-change-listener button
			   (fn [evt]
			     (let [button-value {(keyword (.getActionCommand button))
						 (button-selected? button)}
				   group-value (merge (key @context) button-value)]
			       (update! context {key group-value})))))
    (watch-button-group group context key)))

(defn text-field
  [context key & options]
  (doto (javax.swing.JTextField.)
    (watch-text-field context key)
    (set-properties options)))

(defn listbox
  [context selection-key values-key initial-values & options]
  (let [model (SettableListModel. initial-values)]
    (doto (javax.swing.JList. model)
      (watch-listbox context selection-key values-key)
      (set-properties options))))

(defn text-pane
  [context key & options]
  (doto (javax.swing.JTextPane.)
    (watch-text-pane context key)
    (set-properties options)))

;;;;;;;;;;;;;;;;;;;; Components with no watches ;;;;;;;;;;;;;;;;;;;;

(defn frame
  [& options]
  (doto (javax.swing.JFrame.)
    (set-properties options)))

(defn label
  [& options]
  (doto (javax.swing.JLabel.)
    (set-properties options)))

;; Although JButtons derive from AbstractButtons, they have no state to track.

(defn button
  [& options]
  (doto (javax.swing.JButton.)
    (set-properties options)))

(defn scroll-pane
  [component & options]
  (doto (javax.swing.JScrollPane. component)
    (set-properties options)))