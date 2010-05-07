(ns seesaw.models-test
  (:import [seesaw.models SettableListModel]
	   [javax.swing.event ListDataListener ListDataEvent])
  (:use [clojure.test]))

(deftest list-model-sets-elements
  (let [model (SettableListModel. [])]
    (.setElements model (range 4))
    (is (= (.getElements model)
	   (range 4)))))

(deftest list-model-gets-size
  (let [model (SettableListModel. (range 4))]
    (is (= (.getSize model)
	   4))))

(deftest list-model-notifies-listeners
  (let [model (SettableListModel. [])
	ref (ref false)
	ran-fn #(dosync (ref-set ref %))]
    (.addListDataListener model (proxy [ListDataListener] []
				  (contentsChanged [evt] (ran-fn evt))
				  (intervalAdded [evt] (ran-fn evt))
				  (intervalRemoved [evt] (ran-fn evt))))
    (.setElements model (range 3))
    (are [x y] (= x y)
	 0 (.getIndex0 @ref)
	 2 (.getIndex1 @ref)
	 ListDataEvent/CONTENTS_CHANGED (.getType @ref))))

(deftest list-model-accepts-initial-elements
  (let [model (SettableListModel. (range 4))]
    (is (= (.getElements model)
	   (range 4)))))