(ns seesaw.core
  (:use [seesaw listeners spectator watch component-utils utils])
  (:import [javax.swing JCheckBox JTextField JFrame JLabel JRadioButton ButtonGroup]))

;;;;;;;;;;;;;;;;;;;; Components with watches ;;;;;;;;;;;;;;;;;;;;

(defn checkbox 
  ([context key]
     (watch-checkbox (JCheckBox.) context key))
  ([context key arg0]
     (watch-checkbox (JCheckBox. arg0) context key))
  ([context key arg0 arg1]
     (watch-checkbox (JCheckBox. arg0 arg1) context key))
  ([context key arg0 arg1 arg2]
     (watch-checkbox (JCheckBox. arg0 arg1 arg2) context key)))

(defn text-field
  ([context key]
     (watch-text-field (JTextField.) context key))
  ([context key arg0]
     (watch-text-field (JTextField. arg0) context key))
  ([context key arg0 arg1]
     (watch-text-field (JTextField. arg0 arg1) context key))
  ([context key arg0 arg1 arg2]
     (watch-text-field (JTextField. arg0 arg1 arg2) context key)))

(defn radio-button
  ([key]
     (set-action-command (JRadioButton.) (keyword-str key))))

(defn button-group
  ([context key value-fn & buttons]
     (let [group (ButtonGroup.)]
       (doseq [button buttons]
	 (.add group button)
	 (add-change-listener
	  button
	  (fn [evt]
	    (let [button-value {(keyword (.getActionCommand button))
				(value-fn button)}
		  group-value (merge (key @context) button-value)]
	      (update! context {key group-value})))))
       (watch-button-group group context key value-fn))))

;;;;;;;;;;;;;;;;;;;; Components with no watches ;;;;;;;;;;;;;;;;;;;;

(defn frame
  ([]
     (JFrame.))
  ([arg0]
     (JFrame. arg0))
  ([arg0 arg1]
     (JFrame. arg0 arg1)))

(defn label
  ([]
     (JLabel.))
  ([arg0]
     (JLabel. arg0))
  ([arg0 arg1]
     (JLabel. arg0 arg1))
  ([arg0 arg1 arg2]
     (JLabel. arg0 arg1 arg2)))