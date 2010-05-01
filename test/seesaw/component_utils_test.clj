(ns seesaw.component-utils-test
  (:use [seesaw component-utils] :reload-all)
  (:use [clojure.test])
  (:import [javax.swing JCheckBox JTextField JRadioButton ButtonGroup]))

(deftest checkbox-selected?-is-correct
  (let [checkbox (JCheckBox.)]
    (.setSelected checkbox false)
    (is (not (checkbox-selected? checkbox)))
    
    (.setSelected checkbox true)
    (is (checkbox-selected? checkbox))))

(deftest select-checkbox-selects
  (let [checkbox (JCheckBox.)]
    (select-checkbox checkbox true)
    (is (.isSelected checkbox))

    (select-checkbox checkbox false)
    (is (not (.isSelected checkbox)))))

(deftest text-field-value-gets-text
  (let [text-field (JTextField.)]
    (.setText text-field "Lorem ipsum")
    (is (= (text-field-value text-field)
	   "Lorem ipsum"))))

(deftest set-text-field-sets-text
  (let [text-field (JTextField.)]
    (set-text-field text-field "...dolor sit amet")
    (is (= (.getText text-field)
	   "...dolor sit amet"))))

(deftest radio-button-selected?-is-correct
  (let [button (JRadioButton.)]
    (.setSelected button false)
    (is (not (radio-button-selected? button)))

    (.setSelected button true)
    (is (radio-button-selected? button))))

(deftest select-radio-button-selects
  (let [button (JRadioButton.)]
    (select-radio-button button true)
    (is (.isSelected button))

    (select-radio-button button false)
    (is (not (.isSelected button)))))

(deftest add-to-group-adds
  (let [group (ButtonGroup.)
	button (JRadioButton.)]
    (add-to-group button group)
    (is (= (first (enumeration-seq (.getElements group)))
	   button))))

(deftest set-action-command-sets
  (let [button (JRadioButton.)]
    (set-action-command button "Action!")
    (is (= (.getActionCommand button)
	   "Action!"))))

(deftest button-group-buttons-gets-buttons
  (let [b1 (JRadioButton.)
	b2 (JRadioButton.)
	group (ButtonGroup.)]
    (doto group
      (.add b1)
      (.add b2))
    (let [buttons (button-group-buttons group)]
      (is (some #(= %1 b1) buttons))
      (is (some #(= %1 b2) buttons)))))

(deftest button-group-buttons-gets-nil
  (let [group (ButtonGroup.)]
    (is (nil? (button-group-buttons group)))))

(deftest find-button-finds-button
  (let [b1 (JRadioButton.)
	b2 (JRadioButton.)
	group (ButtonGroup.)]
    (.setActionCommand b1 "b1")
    (.setActionCommand b2 "b2")
    (doto group
      (.add b1)
      (.add b2))
    (is (= (find-button group "b1")
	   b1))
    (is (= (find-button group "b2")
	   b2))
    (is (nil? (find-button group "b3")))))

