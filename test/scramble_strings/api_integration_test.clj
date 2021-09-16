(ns scramble-strings.api-integration-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [clojure.data.json :as json]
            [scramble-strings.core :refer :all]))

(deftest scramble-string-mock-API
  (is (=  (app (-> (mock/request :get "/scramble?first=nandankumar&second=ankum"))))
      {:status  200
        :headers {"content-type" "application/json"}
        :body    (json/write-str {:first-word "nandankumar" :second-word "ankum" :is-scrambled? true})})
  (is (=  (app (-> (mock/request :get "/scramble?first=nandankumar&second=steak"))))
      {:status  200
       :headers {"content-type" "application/json"}
       :body    (json/write-str {:first-word "nandankumar" :second-word "steak" :is-scrambled? false})})
  (is (=  (app (-> (mock/request :get "/scramble?first=nandank*umar&second=kum"))))
      {:status  200
       :headers {"content-type" "application/json"}
       :body    (json/write-str {:first-word "nandank*umar" :second-word "kum" :is-scrambled? false})}))
