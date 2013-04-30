# clomponents

[![Build Status](https://secure.travis-ci.org/mccraigmccraig/clomponents.png)](http://travis-ci.org/mccraigmccraig/clomponents)


Stupid simple component management for clojure apps

## Usage

a component is some resource with a create/destroy lifecycle. clomponents gives you a registry of components, and
a way of defining functions to create/destroy/do-other-things the components

each component is defined by a config hash. there must at least be a create function for the component, which takes a single argumen (the config), and can be found by

* namespace qualified reference : `{:create 'foo/create-me}`
* default namespace and unqualified reference : `{:ns 'foo :create 'create-me}`
* default name `create` in namespace : `{:ns 'foo}` will use `'foo/create`
* literal function : `{:create (fn [config] "do something"}`

a destroy function may also be defined, which takes two arguments : the config and the object

further arbitrary action functions may be defined, and they will also take the same two arguments as destroy : the config and object

   (require '[clomponents.core :as clom])

   ;; define some components
   (def comps {:foo {:ns 'foo}

               :bar {:create 'bar/create-me :destroy 'bar/destroy-me :meep 'bar/meep-me}

               :baz {:create (fn [config] {:baz config})
                     :bloop (fn [config obj] (prn "bloop!") (prn config) (prn obj))}

   (def registry (clom/create-registry comps))

   ;; instantiate components
   (create registry :foo)
   (create registry :bar)
   (create registry :baz)

   ;; retrieve component objects
   (object registry :foo)

   ;; do some arbitrary actions
   (perform registry :bar :meep)
   (perform registry :baz :bloop)

   ;; tear down the components
   (destroy registry :foo)
   (destroy registry :bar)
   (destroy registry :baz)


## License

Copyright © 2013 mccraigmccraig of the clan mccraig

Distributed under the Eclipse Public License, the same as Clojure.
