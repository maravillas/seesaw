(ns seesaw.core
  (:use [seesaw listeners spectator watch component-utils utils])
  (:import [javax.swing JCheckBox JTextField JFrame JLabel JRadioButton ButtonGroup]))

(defn- set-properties
  [component properties-values]
  (doseq [[prop val] (partition 2 properties-values)]
    (let [val (if (coll? val) val [val])]
      (apply str-invoke (keyword-to-setter prop) component val)))
  component)

;;;;;;;;;;;;;;;;;;;; Components with watches ;;;;;;;;;;;;;;;;;;;;

(defn checkbox 
  ([context key & options]
     (-> (JCheckBox.)
	 (watch-checkbox context key)
	 (set-properties options))))

(defn text-field
  ([context key & options]
     (-> (JTextField.)
	 (watch-text-field context key)
	 (set-properties options))))

(defn radio-button
  ([key & options]
     (-> (JRadioButton.)
	 (set-properties options)
	 (set-action-command (keyword-str key)))))

(defn button-group
  ([context key value-fn & buttons]
     (let [group (ButtonGroup.)]
       (doseq [button buttons]
	 (.add group button)
	 (add-change-listener button
			      (fn [evt]
				(let [button-value {(keyword (.getActionCommand button))
						    (value-fn button)}
				      group-value (merge (key @context) button-value)]
	      (update! context {key group-value})))))
       (watch-button-group group context key value-fn))))

;;;;;;;;;;;;;;;;;;;; Components with no watches ;;;;;;;;;;;;;;;;;;;;

(defn frame
  ([& options]
     (-> (JFrame.)
	 (set-properties options))))

(defn label
  ([& options]
     (-> (JLabel.)
	 (set-properties options))))