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

(defn checkbox 
  [context key & options]
  (doto (JCheckBox.)
    (watch-checkbox context key)
    (set-properties options)))

(defn text-field
  [context key & options]
  (doto (JTextField.)
    (watch-text-field context key)
    (set-properties options)))

(defn radio-button
  [context key & options]
  (doto (JRadioButton.)
    (watch-button context key)
    (set-properties options)
    (set-action-command (keyword-str key))))

(defn toggle-button
  [context key & options]
  (doto (JToggleButton.)
    (watch-button context key)
    (set-properties options)
    (set-action-command (keyword-str key))))

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

;;;;;;;;;;;;;;;;;;;; Components with no watches ;;;;;;;;;;;;;;;;;;;;

(defn frame
  [& options]
  (doto (JFrame.)
    (set-properties options)))

(defn label
  [& options]
  (doto (JLabel.)
    (set-properties options)))

(defn button
  [& options]
  (doto (JButton.)
    (set-properties options)))