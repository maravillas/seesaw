(ns seesaw.utils
  (:use [clojure.contrib.str-utils2 :only [split capitalize]]))

;; see clojure.contrib.string/as-str in 1.2

(defn keyword-str
  "Returns the string representation of a keyword without the : prefix."
  [k]
  (subs (str k) 1))

(defn camel-case
  "Returns a CamelCased string from a dash-delimited string."
  [string]
  (let [words (split string #"-")]
    (apply str (map capitalize words))))

(defn keyword-to-setter
  [keyword]
  (str "set" (camel-case (keyword-str keyword))))

;; http://groups.google.com/group/clojure/msg/a72156bb7e900195

(defn str-invoke [method-str instance & args]
  (clojure.lang.Reflector/invokeInstanceMethod instance method-str (to-array args)))
