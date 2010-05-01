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

(deftest radio-button-group-initializes-context
  (let [context (make-context)
	button1 (radio-button :a)
	button2 (radio-button :b)
	button3 (radio-button :c)
	group (button-group context :group button1 button2 button3)]
    (is (= (:group @context)
	   {:a false :b false :c false}))))

(deftest radio-button-changes-update-context
  (let [context (make-context)
	button1 (radio-button :a)
	button2 (radio-button :b)
	button3 (radio-button :c)
	group (button-group context :group button1 button2 button3)]
    (.setSelected button1 true)
    (is (= (:group @context)
	   {:a true :b false :c false}))))

(deftest radio-button-mirrors-context
  (let [context (make-context)
	button1 (radio-button :a)
	button2 (radio-button :b)
	button3 (radio-button :c)
	group (button-group context :group button1 button2 button3)
	agent (agent nil)]
    (update! context {:group {:a false :b true :c false}} false agent)
    (await agent)
    (is (= (:group @context)
	   {:a false :b true :c false}))))