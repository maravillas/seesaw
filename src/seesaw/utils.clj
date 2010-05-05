(ns seesaw.utils
  (:use [clojure.contrib.str-utils2 :only [split capitalize]]))

(defn camel-case
  "Returns a CamelCased string from a dash-delimited string."
  [string]
  (let [words (split string #"-")]
    (apply str (map capitalize words))))

(defn keyword-to-setter
  [keyword]
  (str "set" (camel-case (name keyword))))

;; http://groups.google.com/group/clojure/msg/a72156bb7e900195

(defn str-invoke [method-str instance & args]
  (clojure.lang.Reflector/invokeInstanceMethod instance method-str (to-array args)))
