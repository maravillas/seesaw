(ns seesaw.core
  (:use [seesaw listeners spectator watch component-utils utils])
  (:import [seesaw.models SettableListModel]))

;; Helpful functions

(defn set-properties
  [component properties-values]
  (doseq [[prop val] (partition 2 properties-values)]
    (let [val (if (coll? val) val [val])]
      (apply str-invoke (keyword-to-setter prop) component val))))

(defn use-system-look-and-feel
  []
  (javax.swing.UIManager/setLookAndFeel
   (javax.swing.UIManager/getSystemLookAndFeelClassName)))

(defn add-to
  [parent & children-constraints]
  {:pre [(even? (count children-constraints))]}
  (doseq [[child constraint] (partition 2 children-constraints)]
    (.add parent child constraint)))

(defn prepare-frame
  [frame layout & children]
  (apply add-to frame children)
  (doto frame
    (.setDefaultCloseOperation javax.swing.JFrame/EXIT_ON_CLOSE)
    (.setLayout layout)
    .pack
    .show))

;; Components with watches

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


;; Components without watches

(defn frame
  [& options]
  (doto (javax.swing.JFrame.)
    (set-properties options)))

(defn label
  [& options]
  (doto (javax.swing.JLabel.)
    (set-properties options)))

(defn button
  [& options]
  (doto (javax.swing.JButton.)
    (set-properties options)))

(defn scroll-pane
  [component & options]
  (doto (javax.swing.JScrollPane. component)
    (set-properties options)))

(defn panel
  [& options]
  (doto (javax.swing.JPanel.)
    (set-properties options)))

;; Layout

(defn mig-layout
  ([]
     (net.miginfocom.swing.MigLayout.))
  ([layout-constraints]
     (net.miginfocom.swing.MigLayout. layout-constraints))
  ([layout-constraints column-constraints]
     (net.miginfocom.swing.MigLayout. layout-constraints
				      column-constraints))
  ([layout-constraints column-constraints row-constraints]
     (net.miginfocom.swing.MigLayout. layout-constraints
				      column-constraints
				      row-constraints)))