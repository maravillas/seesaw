(ns seesaw.core
  (:use [seesaw listeners spectator watch component-utils utils])
  (:import [javax.swing JCheckBox JTextField JFrame JLabel JRadioButton ButtonGroup JButton]))

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
    (watch-radio-button context key)
    (set-properties options)
    (set-action-command (keyword-str key))))

(defn button-group
  [context key value-fn & buttons]
  (let [group (ButtonGroup.)]
    (doseq [button buttons]
      (.add group button)
      (add-change-listener button
			   (fn [evt]
			     (let [button-value {(keyword (.getActionCommand button))
						 (value-fn button)}
				   group-value (merge (key @context) button-value)]
			       (update! context {key group-value})))))
    (watch-button-group group context key value-fn)))

(defn radio-button-group
  [context key & buttons]
  (apply button-group context key radio-button-selected? buttons))

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