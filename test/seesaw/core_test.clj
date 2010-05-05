(ns seesaw.core-test
  (:use [seesaw core spectator component-utils] :reload-all)
  (:use [clojure.test]))

(defn abstract-button-initializes-context
  [button-fn]
  (let [context (make-context)
	button (button-fn context :button :selected false)]
    (is (contains? @context :button))
    (is (not (:button @context)))))

(deftest checkbox-initializes-context
  (abstract-button-initializes-context checkbox))

(deftest radio-button-initializes-context
  (abstract-button-initializes-context radio-button))

(deftest toggle-button-initializes-context
  (abstract-button-initializes-context toggle-button))

(defn abstract-button-changes-update-context
  [button-fn]
  (let [context (make-context)
	button (button-fn context :button :selected false)]
    (.setSelected button true)
    (is (:button @context))))

(deftest checkbox-changes-update-context
  (abstract-button-changes-update-context checkbox))

(deftest radio-button-changes-update-context
  (abstract-button-changes-update-context radio-button))

(deftest toggle-button-changes-update-context
  (abstract-button-changes-update-context toggle-button))

(defn abstract-button-mirrors-context
  [button-fn]
  (let [context (make-context)
	button (button-fn context :button)
	agent (agent nil)]
    (update! context {:button true} false agent)
    (await agent)
    (is (.. button isSelected))))

(deftest checkbox-mirrors-context
  (abstract-button-mirrors-context checkbox))

(deftest radio-button-mirrors-context
  (abstract-button-mirrors-context radio-button))

(deftest toggle-button-mirrors-context
  (abstract-button-mirrors-context toggle-button))

(defn abstract-button-options-are-set
  [button-fn]
  (let [context (make-context)
	button (button-fn context :button :selected true :text "text")]
    (is (.isSelected button))
    (is (= (.getText button)
	   "text"))))

(deftest checkbox-options-are-set
  (abstract-button-options-are-set checkbox))

(deftest radio-button-options-are-set
  (abstract-button-options-are-set radio-button))

(deftest toggle-button-options-are-set
  (abstract-button-options-are-set toggle-button))

(deftest button-group-initializes-context
  (let [context (make-context)
	button1 (radio-button context :a :selected true)
	button2 (radio-button context :b)
	button3 (radio-button context :c)
	group (button-group context :group button1 button2 button3)]
    (is (= (:group @context)
	   {:a true :b false :c false}))))

(deftest button-group-changes-update-context
  (let [context (make-context)
	button1 (radio-button context :a)
	button2 (radio-button context :b)
	button3 (radio-button context :c)
	group (button-group context :group button1 button2 button3)]
    (.setSelected button1 true)
    (is (= (:group @context)
	   {:a true :b false :c false}))))

(deftest button-group-mirrors-context
  (let [context (make-context)
	button1 (radio-button context :a)
	button2 (radio-button context :b)
	button3 (radio-button context :c)
	group (button-group context :group button1 button2 button3)
	agent (agent nil)]
    (update! context {:group {:a false :b true :c false}} false agent)
    (await agent)
    (is (= (:group @context)
	   {:a false :b true :c false}))))

(deftest text-field-initializes-context
  (let [context (make-context)
	text-field (text-field context :text :text "text")]
    (is (contains? @context :text))
    (is (= (:text @context)
	   "text"))))

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

(deftest text-field-options-are-set
  (let [context (make-context)
	text-field (text-field context :text :text "text" :enabled false)]
    (is (= (.getText text-field)
	   "text"))
    (is (not (.isEnabled text-field)))))

(deftest listbox-initializes-context
  ; Next up
  )



(deftest frame-options-are-set
  (let [frame (frame :size [2 4] :visible false)]
    (is (= (.getSize frame)
	   (java.awt.Dimension. 2 4)))
    (is (not (.isVisible frame)))))

(deftest label-options-are-set
  (let [label (label :text "text" :tool-tip-text "tool tip!")]
    (is (= (.getText label)
	   "text"))
    (is (= (.getToolTipText label)
	   "tool tip!"))))

(deftest button-options-are-set
  (let [button (button :text "text" :enabled false)]
    (is (= (.getText button)
	   "text"))
    (is (not (.isEnabled button)))))

