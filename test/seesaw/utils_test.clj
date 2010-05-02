(ns seesaw.utils-test
  (:use [seesaw utils] :reload-all)
  (:use [clojure.test]))

(deftest keyword-str-drops-one-colon
  (is (= (keyword-str :keyword)
	 "keyword"))
  (is (= (keyword-str ::a)
	 "seesaw.utils-test/a"))
  (is (= (keyword-str :f:oo)
	 "f:oo")))

(deftest camel-case-camel-cases
  (is (= (camel-case "a-dashed-string")
	 "ADashedString"))
  (is (= (camel-case "oneword")
	 "Oneword")))

(deftest camel-case-ignores-empty-portions
  (is (= (camel-case "")
	 ""))
  (is (= (camel-case "-")
	 ""))
  (is (= (camel-case "-foo")
	 "Foo"))
  (is (= (camel-case "foo-")
	 "Foo"))
  (is (= (camel-case "-foo-")
	 "Foo")))

(deftest keyword-to-setter-transforms-keyword
  (is (= (keyword-to-setter :text)
	 "setText"))
  (is (= (keyword-to-setter :label-for)
	 "setLabelFor")))

(deftest str-invoke-calls-java-method
  (let [checkbox (javax.swing.JCheckBox.)]
    (str-invoke "setText" checkbox "text")
    (is (= (.getText checkbox)
	   "text"))))