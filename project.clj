(defproject seesaw/seesaw "1.0.0-SNAPSHOT"
  :description "A library for building Swing applications in Clojure."
  :dependencies [[org.clojure/clojure "1.1.0"]
                 [org.clojure/clojure-contrib "1.1.0"]
		 [commons-logging/commons-logging "1.1"]
		 [spectator/spectator "1.1.0-SNAPSHOT"]]
  :dev-dependencies [[ant/ant "1.6.5"]
                     [ant/ant-launcher "1.6.5"]
                     [leiningen/lein-swank "1.2.0-SNAPSHOT"]
		     [lein-clojars "0.5.0"]]
  :repositories [["clojars" "http://clojars.org/repo"]])
