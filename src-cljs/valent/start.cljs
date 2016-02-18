(ns valent.start
  (:require [sablono.core :as sab]
            [valent.core :as valent]
            [valent.model :as model]
            [taoensso.timbre :as timbre
             :refer-macros (spy)]
            cljsjs.react.dom))

(def loc-selector
  {:id identity
   :city identity})

(def person-selector
  {:id 0
   :name ""
   :loc loc-selector
   })

(defn greet [person]
  (sab/html
   [:div
    {:key (:id person)}
    [:p "Name of person: " (:name person)]
    [:p "...who lives in " (get-in person [:loc :city])]
    ]))

(def ppl-query
  [person-selector])

(defn ppl [people]
  (sab/html
   [:div
    (for [person people]
      (greet person))]))

(def app-query
  {:people ppl-query})

(defn app [state]
  (ppl (:people state)))

(defn render! []
  (.render
   js/ReactDOM
   (app (valent/root app-query @model/app-state model/joins))
   (.getElementById js/document "app")))

(add-watch model/app-state :on-state-change (fn [_ _ _ _] (render!)))

(enable-console-print!)

(render!)
