(ns seesaw.component-utils)

(defn checkbox-selected? [checkbox]
  (.isSelected checkbox))

(defn select-checkbox [checkbox selected]
  (.setSelected checkbox selected))


(defn text-field-value [text-field]
  (.getText text-field))

(defn set-text-field [text-field text]
  (.setText text-field text))


(defn radio-button-selected? [radio-button]
  (.isSelected radio-button))

(defn select-radio-button [radio-button selected]
  (.setSelected radio-button selected))

(defn add-to-group [button button-group]
  (.add button-group button)
  button)

(defn set-action-command [button command]
  (.setActionCommand button command)
  button)


(defn button-group-buttons [button-group]
  (enumeration-seq (.getElements button-group)))

(defn find-button [button-group action-command]
  (some #(and (= (.getActionCommand %1) action-command) %1)
	(button-group-buttons button-group)))

(defn button-group-value [button-group]
  (apply hash-map (mapcat (fn [button] [(keyword (.getActionCommand button))
					(radio-button-selected? button)])
			  (enumeration-seq (.getElements button-group)))))

(defn set-button-group [button-group new-value]
  (doseq [key (keys new-value)]
    (let [button (find-button button-group key)
	  model (.getModel button)
	  value (key new-value)]
      (.setSelected button-group model value))))