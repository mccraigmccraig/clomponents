(ns clomponents.core-test
  (:use midje.sweet
        clomponents.core)
  (:require [clojure.tools.logging :as log]
            [clomponents.control :as control]))

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

(fact "swap-registry should destroy the old registry clomponents before creating new"
  (let [config {:foo {:id ..idfoo..
                      :create (fn [c] (fact (:id c) => ..idfoo..) ..fooobj..)}
                :bar {:id ..idbar..
                      :create (fn [c] (fact (:id c) => ..idbar..) ..barobj..)}
                :baz {:id ..idbaz..
                      :create (fn [c] (fact (:id c) => ..idbaz..) ..bazobj..)}}
        ratom (atom (create-registry config))
        foo-clomp (@ratom :foo)
        bar-clomp (@ratom :bar)
        baz-clomp (@ratom :baz)]

    (control/create foo-clomp) => ..fooobj..
    (control/create bar-clomp) => ..barobj..

    (swap-registry ratom config) => anything
    (provided
      (control/destroy baz-clomp) => nil
      (control/destroy bar-clomp) => nil
      (control/destroy foo-clomp) => nil)

    (@ratom :foo) =not=> foo-clomp
    (create @ratom :foo) => ..fooobj..

    (@ratom :bar) =not=> bar-clomp
    (create @ratom :bar) => ..barobj..

    (@ratom :baz) =not=> baz-clomp
    (create @ratom :baz) => ..bazobj..
    ))
