(ns seesaw.models
  (:import [javax.swing.event ListDataEvent]))

(gen-class
 :name seesaw.models.SettableListModel
 :implements [javax.swing.ListModel]
 :prefix "list-model-"
 :init init
 :constructors {[clojure.lang.Seqable] []}
 :methods [[setElements [clojure.lang.Seqable] void]
	   [getElements [] clojure.lang.Seqable]]
 :state state)

(defn list-model-init
  ([elements]
     [[] (ref {:listeners [] :elements elements})]))

(defn list-model-addListDataListener
  [this listener]
  (dosync
   (commute (.state this)
	    (fn [val] (assoc val :listeners (cons listener (:listeners val)))))))

(defn list-model-removeListDataListener
  [this listener]
  (dosync
   (commute (.state this)
	    (fn [val] (assoc val :listeners (remove #(= listener %) (:listeners val)))))))

(defn list-model-getElementAt
  [this index]
  (nth (.getElements this) index))

(defn list-model-getSize
  [this]
  (count (.getElements this)))

(defn list-model-setElements
  [this elements]
  (dosync
   (ref-set (.state this) (assoc @(.state this) :elements (seq elements))))
  (let [listeners (:listeners @(.state this))
	event (ListDataEvent. this ListDataEvent/CONTENTS_CHANGED 0 (dec (.getSize this)))]
    (doseq [listener listeners]
      (.contentsChanged listener event))))

(defn list-model-getElements
  [this]
  (:elements @(.state this)))