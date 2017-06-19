(ns fun.imagej.video
  (:require [fun.imagej.imp :as ij1]))        

; This expects that you've made the calls to activate FFMPEG via FIJI
; Super hard-coded for now

(defn save-z-as-avi
  "Save a Z-stack as an avi."
  [imp filename]
  (ij.IJ/run imp "AVI... " (str "compression=JPEG frame=7 save=" filename )))

(defn open-tga-directory
  "Open a directory of tga files as an imagestack."
  [directory]
  (let [listing (.listFiles (java.io.File. directory))]
    (ij1/zconcat-imps
      (for [file (rest listing)]; personal convenience hack for a project... shame TODO
        (let [imp (ij1/open-imp (.getAbsolutePath file))
              imp2 (.createImagePlus imp)
              dim (.getDimensions imp)]
          (.setStack imp2 (ij1/get-title imp) (.getImageStack imp))
          (.setDimensions imp2 (nth dim 2) (nth dim 3) (nth dim 4))
          (.setColorModel (.getProcessor imp2) (.getDefaultColorModel (.getProcessor imp)))
          (.setOpenAsHyperStack imp2 false)
          (let [conv ^ij.process.ImageConverter. (ij.process.ImageConverter. imp2)]
            (.convertRGBStackToRGB conv)
            imp2
            ))))))

(defn tga-sequence-to-avi
  "Take a TGA (RAW) sequence as a directory, and make an avi."
  [directory avi-filename]
  (let [imp (open-tga-directory directory)]
    (save-z-as-avi imp avi-filename)))

