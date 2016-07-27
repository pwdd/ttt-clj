(ns ttt-clj.negamax
  (:require [ttt-clj.rules :as rules]
            [ttt-clj.board :as board]))

(def start-depth 0)

(defn board-analysis
  [board current-player opponent depth]
  (if (rules/draw? board)
    0
    (let [winner (rules/winner board)]
      (if (= winner current-player)
        (- 10 depth)
        (- depth 10)))))

(declare negamax)

(defn negamax-scores
  [board current-player opponent depth]
  (let [spots (board/available-spots board)
        new-boards (map #(board/move board % current-player) spots)]
    (map #(- (negamax % opponent current-player (inc depth))) new-boards)))

(defn max-score
  [board current-player opponent depth]
  (if (rules/game-over? board)
    (board-analysis board current-player opponent depth)
    (apply max (negamax-scores board
                               current-player
                               opponent
                               depth))))

(def negamax (memoize max-score))

(defn best-move
  [board current-player opponent depth]
  (let [spots (board/available-spots board)
        scores (negamax-scores board current-player opponent depth)
        max-value (apply max scores)
        best (.indexOf scores max-value)]
    (nth spots best)))
