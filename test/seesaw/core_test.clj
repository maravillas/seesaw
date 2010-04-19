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

(deftest text-field-initializes-context
  (let [context (make-context)
	text-field (text-field context :text)]
    (is (contains? @context :text))
    (is (= (:text @context)
	   ""))))

(deftest text-field-changes-update-context
  (let [context (make-context)
	text-field (text-field context :text)]
    (.setText text-field "Lorem Ipsum")
    (is (= (:text @context)
	   "Lorem Ipsum"))))

(deftest text-field-mirrors-context
  (let [context (make-context)
	text-field (text-field context :text)
	agent (agent nil)]
    (update! context {:text "Lorem Ipsum"} false agent)
    (await agent)
    (is (= (.getText text-field)
	   "Lorem Ipsum"))))