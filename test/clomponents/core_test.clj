(ns clomponents.core-test
  (:use midje.sweet
        clomponents.core)
  (:require [clojure.tools.logging :as log]))

(fact "create-registry should return a hash of clomponents"
  (let [config {:foo {:id ..idfoo..
                      :create (fn [c] (fact (:id c) => ..idfoo..) ..fooobj..)
                      :fibblerise (fn [c o] (fact (:id c) => ..idfoo.. o => ..fooobj..) ..fibblerise..)}
                :bar {:id ..idbar..
                      :create (fn [c] (fact (:id c) => ..idbar..) ..barobj..)
                      :destroy (fn [c o] (fact (:id c) => ..idbar.. o => ..barobj..) ..destroy-bar..)}}
        r (create-registry config)]

    (create r :foo) => ..fooobj..
    (object r :foo) => ..fooobj..
    (perform r :foo :fibblerise) => ..fibblerise..
    (destroy r :foo) => nil

    (create r :bar) => ..barobj..
    (object r :bar) => ..barobj..
    (destroy r :bar) => ..destroy-bar..))
