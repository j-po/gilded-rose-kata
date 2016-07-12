(ns gilded-rose.core)

(defn update-quality [items]
  (map
    (fn [{:keys [name quality sell-in] :as item}]
      (update item :quality (case name
                              "Backstage passes to a TAFKAL80ETC concert" (cond
                                                                            (< sell-in 0) #(0)
                                                                            (or (>= sell-in 10) (= quality 50)) identity
                                                                            (< sell-in 5) #(+ 3 %)
                                                                            (< sell-in 10) #(+ 2 %))
                              "Aged Brie" (if (= quality 50)
                                            identity
                                            inc)
                              "Sulfuras, Hand of Ragnaros" identity
                              (if conjured?
                                (if (< sell-in 0)
                                  #(- % 4)
                                  #(- % 2))
                                (if (< sell-in 0)
                                  #(- % 2)
                                  dec)))))
    (map (fn [item]
           (if (not= "Sulfuras, Hand of Ragnaros" (:name item))
             (merge item {:sell-in (dec (:sell-in item))})
             item))
         items)))

(defn item [item-name, sell-in, quality]
  {:name item-name, :sell-in sell-in, :quality quality})

(defn conjured [item]
  (merge item {:conjured? true}))

(defn update-current-inventory[]
  (let [inventory 
    [
      (item "+5 Dexterity Vest" 10 20)
      (conjured (item "+5 Dexterity Vest" 10 20))
      (item "Aged Brie" 2 0)
      (item "Elixir of the Mongoose" 5 7)
      (conjured (item "Elixir of the Mongoose" 5 7))
      (item "Sulfuras, Hand Of Ragnaros" 0 80)
      (item "Backstage passes to a TAFKAL80ETC concert" 15 20)
    ]]
    (update-items inventory)))
