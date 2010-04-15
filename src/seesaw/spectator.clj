(ns seesaw.spectator
  (:require [spectator.core :as spectator]))

(defn make-context
  []
  (atom {}))

(defn update!
  ([context updates]
     (update! context updates false))
  ([context updates silent]
     (swap! context spectator/update updates silent)))

(defn add-updater!
  [context f & keys]
  (apply swap! context spectator/add-updater f keys))

(defn add-observer!
  [context f & keys]
  (apply swap! context spectator/add-observer f keys))