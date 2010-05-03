(ns seesaw.core
  (:use [seesaw listeners spectator watch component-utils utils])
  (:import [javax.swing JCheckBox JTextField JFrame JLabel JRadioButton
	    ButtonGroup JButton JToggleButton]))

(defn- set-properties
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
    (set-action-command (keyword-str key))))

(defn checkbox 
  [context key & options]
  (apply abstract-button (JCheckBox.) context key options))

(defn radio-button
  [context key & options]
  (apply abstract-button (JRadioButton.) context key options))

(defn toggle-button
  [context key & options]
  (apply abstract-button (JToggleButton.) context key options))

(defn button-group
  [context key & buttons]
  (let [group (ButtonGroup.)]
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
  (doto (JTextField.)
    (watch-text-field context key)
    (set-properties options)))

;;;;;;;;;;;;;;;;;;;; Components with no watches ;;;;;;;;;;;;;;;;;;;;

(defn frame
  [& options]
  (doto (JFrame.)
    (set-properties options)))

(defn label
  [& options]
  (doto (JLabel.)
    (set-properties options)))

;; Although JButtons derive from AbstractButtons, they have no state to track.

(defn button
  [& options]
  (doto (JButton.)
    (set-properties options)))