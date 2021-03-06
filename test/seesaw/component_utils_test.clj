(ns seesaw.component-utils-test
  (:use [seesaw component-utils] :reload-all)
  (:use [clojure.test])
  (:import [javax.swing JCheckBox JTextField JRadioButton ButtonGroup JList ListSelectionModel]
	   [seesaw.models SettableListModel]))

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

(deftest text-value-gets-text
  (let [text-field (JTextField.)]
    (.setText text-field "Lorem ipsum")
    (is (= (text-value text-field)
	   "Lorem ipsum"))))

(deftest set-text-sets-text
  (let [text-field (JTextField.)]
    (set-text text-field "...dolor sit amet")
    (is (= (.getText text-field)
	   "...dolor sit amet"))))

(deftest button-selected?-is-correct
  (let [button (JRadioButton.)]
    (.setSelected button false)
    (is (not (button-selected? button)))

    (.setSelected button true)
    (is (button-selected? button))))

(deftest select-button-selects
  (let [button (JRadioButton.)]
    (select-button button true)
    (is (.isSelected button))

    (select-button button false)
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

(defn- init-radio-button-group
  [b1 b2 group]
    (.setActionCommand b1 "b1")
    (.setActionCommand b2 "b2")
    (doto group
      (.add b1)
      (.add b2)))

(deftest button-group-buttons-gets-buttons
  (let [b1 (JRadioButton.)
	b2 (JRadioButton.)
	group (ButtonGroup.)]
    (init-radio-button-group b1 b2 group)
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
    (init-radio-button-group b1 b2 group)
    (is (= (find-button group :b1)
	   b1))
    (is (= (find-button group :b2)
	   b2))
    (is (nil? (find-button group :b3)))))

(deftest button-group-value-collects-radio-button-values
  (let [b1 (JRadioButton.)
	b2 (JRadioButton.)
	group (ButtonGroup.)]
    (init-radio-button-group b1 b2 group)
    (is (= (button-group-value group)
	   {:b1 false :b2 false}))
    (.setSelected b1 true)
    (is (= (button-group-value group)
	   {:b1 true :b2 false}))))

(deftest set-button-group-sets-radio-button-value
  (let [b1 (JRadioButton.)
	b2 (JRadioButton.)
	group (ButtonGroup.)]
    (init-radio-button-group b1 b2 group)
    (set-button-group group {:b2 true})
    (is (= (button-group-value group)
	   {:b1 false :b2 true}))
    (set-button-group group {:b1 true :b2 false})
    (is (= (button-group-value group)
	   {:b1 true :b2 false}))))

(deftest listbox-values-gets-values
  (let [listbox (JList. (SettableListModel. (range 10)))]
    (is (= (listbox-values listbox)
	   (range 10)))))

(deftest set-listbox-values-sets-values
  (let [listbox (JList. (SettableListModel. []))]
    (set-listbox-values listbox (range 10))
    (is (= (.. listbox getModel getElements)
	   (range 10)))))

(deftest listbox-selection-gets-single-selection
  (let [listbox (JList. (SettableListModel. (range 10)))]
    (.setSelectionMode listbox ListSelectionModel/SINGLE_SELECTION)
    (.setSelectedIndex listbox 3)
    (is (= (listbox-selection listbox)
	   [3]))))

(deftest listbox-selection-gets-single-interval-selection
  (let [listbox (JList. (SettableListModel. (range 10)))]
    (.setSelectionMode listbox ListSelectionModel/SINGLE_INTERVAL_SELECTION)
    (.setSelectionInterval listbox 1 3)
    (is (= (listbox-selection listbox)
	   [1 2 3]))))

(deftest listbox-selection-gets-multiple-interval-selection
  (let [listbox (JList. (SettableListModel. (range 10)))]
    (.setSelectionMode listbox ListSelectionModel/MULTIPLE_INTERVAL_SELECTION)
    (.setSelectedIndices listbox (into-array Integer/TYPE [1 3 4 7]))
    (is (= (listbox-selection listbox)
	   [1 3 4 7]))))

(deftest listbox-selection-returns-nil-when-no-selection
  (let [listbox (JList. (SettableListModel. (range 10)))]
    (is (= (listbox-selection listbox)))))