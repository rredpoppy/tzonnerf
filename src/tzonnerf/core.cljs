(ns tzonnerf.core
    (:require-macros
      [cljs.core.async.macros :refer [go]])
    (:require
      [reagent.core :as r]
      [reagent-forms.core :as f]
      [cljs.core.async :refer [<!]]
      [cljs-http.client :as http]))

(enable-console-print!)
(def state (r/atom {:timezones [] :timezone nil}))

(defn tz [t]
  [:li {:key (:id t) :className "list-group-item" :data-id (:id t)}
    [:strong (:name t)]
    (str " (GMT+" (:offset t) (when (:dst t) " DST") ") ")
    [:button
      {:className "btn btn-danger"
       :on-click (fn [_]
        (go
          (let [
              r (<! (http/delete (str "http://localhost:3000/api/timezones/" (:id t))))]
              (js/location.reload))) 
       )}
       "delete"]])

(defn tz-list []
  [:div
   [:h3 "TimeZones list"]
   [:form.form-inline {:style {:padding "20px 10px"}}
    [:div.form-group
     [:label {:for "name"} "Name"]
     [:input {:type "text" :className "form-control" :id "name" :placeholder "Conf location"}]]
    [:div {:class "form-group"}
     [:label {:for "city"} "City"]
     [:input {:className "form-control" :id "city", :placeholder "Munich"}]]
    [:button {:type "submit", :class "btn btn-default"} "Save"]]
   [:ul {:className "list-group"}
    (map tz (-> @state :timezones))]])
;; -------------------------
;; Views

(defn home-page []
  [:div {:className "wrapper"}
    [:nav {:class "navbar navbar-default"}
      [:div {:class "container-fluid"}
        [:div {:class "navbar-header"}
          [:a {:class "navbar-brand", :href "#"}
            "TimeZonner"]]]]
     [tz-list]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (go (let [r (<! (http/get "http://localhost:3000/api/timezones"))]
    (println r)
    (swap! state (fn [{tzs :timezones tz :timezone}]
      {:timezone tz :timezones (-> r :body)}))))
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
