(ns tzonnerf.core
    (:require
      [reagent.core :as r]
      [reagent-forms.core :as f]))

(def state (r/atom {:timezones [] :timezone nil}))

;; -------------------------
;; Views

(defn home-page []
  [:nav {:class "navbar navbar-default"}
   [:div {:class "container-fluid"}
    [:div {:class "navbar-header"}
     [:a {:class "navbar-brand", :href "#"}
      "TimeZonner"]]]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
