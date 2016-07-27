(ns ttt-clj.rules
  (:require [ttt-clj.board :as board]))

(defn draw?
  [board]
  (and (board/is-board-full? board)
       (not (board/winning-combo board))))

(defn game-over?
  [board]
  (or (draw? board) (board/winning-combo board)))

(defn winner
  [board]
  (nth board (first (board/winning-combo board))))
