
(ns app.comp.container
  (:require [hsl.core :refer [hsl]]
            [respo-ui.style :as ui]
            [respo.macros
             :refer
             [defcomp
              cursor->
              action->
              mutation->
              <>
              div
              button
              textarea
              span
              svg
              g
              path
              rect
              circle
              line
              defs
              marker]]
            [respo.core :refer [create-element]]
            [verbosely.core :refer [verbosely!]]
            [respo.comp.space :refer [=<]]
            [reel.comp.reel :refer [comp-reel]]))

(defn map-point [point] {:cx (:x point), :cy (:y point)})

(def style-point {:r 4, :stroke "#bbf", :fill "transparent"})

(defcomp
 comp-container
 (reel)
 (let [store (:store reel)
       states (:states store)
       start-point (:start store)
       end-point (:end store)
       p1 (:p1 store)
       p2 (:p2 store)]
   (div
    {:style (merge ui/global ui/row)}
    (div
     {:style {:padding 20}}
     (svg
      {:width "600",
       :height "600",
       :on-mousemove (fn [e d! m!]
         (if (some? (:drag-target store))
           (d! :move (let [event (:event e)] {:x (.-offsetX event), :y (.-offsetY event)})))),
       :on-mouseup (action-> :drag-target nil),
       :style {:background-color (hsl 0 0 90)}}
      (defs
       {}
       (marker
        {:id "arrow",
         :viewBox "0 0 10 10",
         :refX 5,
         :refY 5,
         :orient "auto",
         :markerWidth 20,
         :markerHeight 20}
        (path {:d "M0,0 L10,5 L10,5 L0,10 L5,5 Z", :fill (hsl 0 100 50 0.5)})))
      (path
       {:d (str
            "M "
            (:x start-point)
            ","
            (:y start-point)
            " C"
            (:x p1)
            ","
            (:y p1)
            " "
            (:x p2)
            ","
            (:y p2)
            " "
            (:x end-point)
            ","
            (:y end-point)),
        :fill "transparent",
        :stroke "#aaa",
        :stroke-width 1,
        :marker-end "url(#arrow)"})
      (line
       {:x1 (:x start-point),
        :y1 (:y start-point),
        :x2 (:x p1),
        :y2 (:y p1),
        :stroke "#cecece"})
      (line
       {:x1 (:x p2), :y1 (:y p2), :x2 (:x end-point), :y2 (:y end-point), :stroke "#cecece"})
      (circle
       (merge
        style-point
        (map-point start-point)
        {:on-mousedown (fn [e d! m!] (d! :drag-target :start ))}))
      (circle
       (merge
        style-point
        (map-point end-point)
        {:on-mousedown (fn [e d! m!] (d! :drag-target :end ))}))
      (circle
       (merge
        style-point
        (map-point p1)
        {:on-mousedown (fn [e d! m!] (d! :drag-target :p1 ))}))
      (circle
       (merge
        style-point
        (map-point p2)
        {:on-mousedown (fn [e d! m!] (d! :drag-target :p2 ))}))))
    (cursor-> :reel comp-reel states reel {}))))
