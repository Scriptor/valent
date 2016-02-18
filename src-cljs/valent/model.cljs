(ns valent.model
  (:require [valent.core :as valent]))

(defonce app-state (atom {:people [{:id 1 :name "Bob" :loc 1}
                                   {:id 2 :name "Jane" :loc 1}
                                   {:id 3 :name "Asdf" :loc 2}]
                          :locations [{:id 2 :city "Boston"}
                                      {:id 1 :city "NYC"}]}))
(def joins
  {[:people 0 :loc] (fn [state id] (valent/by :id id (:locations state)))})
