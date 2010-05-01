(ns seesaw.util)

;; see clojure.contrib.string/as-str in 1.2

(defn keyword-str [k]
  "Returns the string representation of a keyword without the : prefix."
  (subs (str k) 1))