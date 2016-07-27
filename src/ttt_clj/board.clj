(ns ttt-clj.board
  (:require [clojure.string :as string]))

(def empty_spot :_)
(def board-size 3)
(def board-length (* board-size board-size))

(defn new-board
  []
  (vec (repeat board-length empty_spot)))

(defn move
  [board spot marker]
  (assoc board spot marker))

(defn is-board-empty?
  [board]
  (every? #(= empty_spot %) board))

(defn is-board-full?
  [board]
  (not-any? #(= empty_spot %) board))

(defn is-spot-available?
  [board spot]
  (= empty_spot (nth board spot)))

(defn available-spots
  [board]
  (map first (filter #(= empty_spot (second %)) (map-indexed vector board))))

(defn is-valid-move?
  [board spot]
  (and (>= spot 0)
       (<= spot (count board))
       (is-spot-available? board spot)))

(defn board-rows
  []
  (partition board-size (range board-length)))

(defn board-columns
  []
  (apply map vector (board-rows)))

(defn diagonal-forward
  []
  (loop [row 0
        column 0
        diagonal []]
    (if (>= row board-size)
      diagonal
      (recur (inc row)
             (inc column)
             (conj diagonal
               (get-in (mapv vec (board-rows)) [row column]))))))

(defn diagonal-backward
  []
  (loop [row 0
         column (dec board-size)
         diagonal []]
    (if (>= row board-size)
      diagonal
      (recur (inc row)
             (dec column)
             (conj diagonal
               (get-in (mapv vec (board-rows)) [row column]))))))

(defn board-diagonals
  []
  [(diagonal-forward) (diagonal-backward)])

(defn winning-positions
  []
  (concat (concat (board-rows) (board-columns)) (board-diagonals)))

(defn repeated-markers?
  [board combo]
  (let [combo-to-check (for [idx combo] (nth board idx))]
    (if (not-any? #{empty_spot} combo-to-check)
      (apply = combo-to-check))))

(defn find-repetition
  [board]
  (filter #(repeated-markers? board %) (winning-positions)))

(defn winning-combo
  [board]
  (first (find-repetition board)))
