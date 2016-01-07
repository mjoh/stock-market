 (ns StockMarket)

 (def curr-price (atom 10))
 (def initial-portfolio { :budget 1000 :stocks 100 } )

 (defn increase-price
  [price]
  (+ price 1))

 (defn decrease-price
  [price]
  (- price 1))

 (defn buy-stock
   [portfolio amount price]
   (prn "buying " amount " for " price)
   (prn "new budget: " (- (:budget portfolio) (* amount price)))
   {
      :effect increase-price
      :new-portfolio
        {
          :budget (- (:budget portfolio) (* amount price))
          :stocks (+ (:stocks portfolio) amount)
        }
   })

 (defn sell-stock
   [portfolio amount price]
   (prn "selling " amount " for " price)
   (prn "new budget: " (+ (:budget portfolio) (* amount price)))
   {
    :effect decrease-price
    :new-portfolio
      {
      :budget (+ (:budget portfolio) (* amount price))
      :stocks (- (:stocks portfolio) amount)
      }
   })

 (defn decide-action
   []
   (if (> (rand-int 2) 0)
     buy-stock
     sell-stock))

 (defn decide-amount
   []
   (rand-int 20))

 (defn find-new-price
   [effect price]
   (let
     [new-price (effect price)]
     (prn "new price: " new-price)
     (if (< new-price 1)
       1
       new-price)))

 (defn trade [portfolio]
   (let
     [action (decide-action)
      amount (decide-amount)
      action-result (action portfolio amount @curr-price)
      new-portfolio (:new-portfolio action-result)
      effect (:effect action-result)]
     (swap! curr-price (partial find-new-price effect))
     new-portfolio))

 (loop
   [portfolio initial-portfolio]
    (doseq []
      (Thread/sleep 1000)
      (if (< (:budget portfolio) 0)
        portfolio
        (recur (trade portfolio)))))
