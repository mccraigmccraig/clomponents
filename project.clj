(def shared
  '[
    [org.clojure/tools.logging "0.2.6"]
    [org.clojure/core.incubator "0.1.2"]
    ])

(defproject clomponents "0.1.0"
  :description "component configuration for clojure"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.0.0"

  :url "http://github.com/mccraigmccraig/clomponents"

  :plugins [[lein-midje "3.0.1"]]

  :dependencies ~(conj shared '[org.clojure/clojure "1.5.1"])

  :profiles {:all {:dependencies ~shared}
             :dev {:dependencies [[midje "1.5.1"]]}
             :production {}
             :1.4 {:dependencies [[org.clojure/clojure "1.4.0"]]}
             :1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}}
  )
