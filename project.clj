(def shared
  '[
    [org.clojure/tools.logging "0.2.6"]
    [org.clojure/core.incubator "0.1.3"]
    ])

(defproject clomponents "0.5.1"
  :description "component configuration for clojure"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.0.0"

  :url "http://github.com/mccraigmccraig/clomponents"
  :scm {:name "git"
        :url "http://github.com/mccraigmccraig/clomponents"}

  :plugins [[lein-midje "3.1.3"]]

  :dependencies ~(conj shared '[org.clojure/clojure "1.6.0"])

  :aliases {"with-all-dev" ["with-profile" "1.4,dev:1.5,dev:1.6,dev"]}
  :profiles {:dev {:dependencies [[midje "1.6.3"]]}
             :production {}
             :1.4 {:dependencies [[org.clojure/clojure "1.4.0"]]}
             :1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}
             :1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}}
  )
