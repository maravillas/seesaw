(ns seesaw.core-test
  (:use [seesaw core spectator] :reload-all)
  (:use [clojure.test]))

(deftest checkbox-initializes-context
  (let [context (make-context)
	checkbox (checkbox context :checkbox)]
    (is (contains? @context :checkbox))
    (is (not (:checkbox @context)))))

(deftest checkbox-changes-update-context
  (let [context (make-context)
	checkbox (checkbox context :checkbox)]
    (.setSelected checkbox true)
    (is (:checkbox @context))))

(deftest checkbox-mirrors-context
  (let [context (make-context)
	checkbox (checkbox context :checkbox)
	agent (agent nil)]
    (update! context {:checkbox true} false agent)
    (await agent)
    (is (.. checkbox getModel isSelected))))

(deftest textfield-initializes-context
  (let [context (make-context)
	textfield (textfield context :text)]
    (is (contains? @context :text))
    (is (= (:text @context)
	   ""))))

(deftest textfield-changes-update-context
  (let [context (make-context)
	textfield (textfield context :text)]
    (.setText textfield "Lorem Ipsum")
    (is (= (:text @context)
	   "Lorem Ipsum"))))

(deftest textfield-mirrors-context
  (let [context (make-context)
	textfield (textfield context :text)
	agent (agent nil)]
    (update! context {:text "Lorem Ipsum"} false agent)
    (await agent)
    (is (= (.getText textfield)
	   "Lorem Ipsum"))))