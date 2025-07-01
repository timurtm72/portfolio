import React, { useState, useCallback, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import WebPImage from '../components/WebPImage.tsx';
import { XMarkIcon, ChevronLeftIcon, ChevronRightIcon } from '@heroicons/react/24/outline';

const certificateFilenames = Array.from({ length: 14 }).map((_, idx) =>
    idx === 0 ? 'sert.webp' : `sert${idx}.webp`
);

const certificatePaths = certificateFilenames.map(
    (name) => `/images/projects/webp/${name}`
);

const Certificates: React.FC = () => {
    const [selectedIndex, setSelectedIndex] = useState<number | null>(null);

    const handleOpen = (index: number) => setSelectedIndex(index);
    const handleClose = () => setSelectedIndex(null);

    const handleKeyDown = useCallback(
        (e: KeyboardEvent) => {
            if (selectedIndex === null) return;
            if (e.key === 'Escape') {
                handleClose();
            } else if (e.key === 'ArrowRight') {
                setSelectedIndex((prev) => {
                    if (prev === null) return prev;
                    return (prev + 1) % certificatePaths.length;
                });
            } else if (e.key === 'ArrowLeft') {
                setSelectedIndex((prev) => {
                    if (prev === null) return prev;
                    return (prev - 1 + certificatePaths.length) % certificatePaths.length;
                });
            }
        },
        [selectedIndex]
    );

    useEffect(() => {
        document.addEventListener('keydown', handleKeyDown);
        return () => {
            document.removeEventListener('keydown', handleKeyDown);
        };
    }, [handleKeyDown]);

    return (
        <div className="container mx-auto px-4 py-8">
            <h2 className="text-3xl font-bold mb-6 text-center">Сертификаты</h2>
            <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-4">
                {certificatePaths.map((path, idx) => (
                    <button
                        key={path}
                        onClick={() => handleOpen(idx)}
                        className="group focus:outline-none"
                    >
                        <motion.div
                            whileHover={{ scale: 1.05 }}
                            whileTap={{ scale: 0.95 }}
                            className="rounded-lg overflow-hidden shadow-lg"
                        >
                            <WebPImage
                                src={path}
                                alt={`Сертификат ${idx + 1}`}
                                className="w-full h-48 object-cover"
                            />
                        </motion.div>
                    </button>
                ))}
            </div>

            {/* Modal */}
            <AnimatePresence>
                {selectedIndex !== null && (
                    <motion.div
                        className="fixed inset-0 z-50 bg-black/70 backdrop-blur-sm flex items-center justify-center"
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        exit={{ opacity: 0 }}
                    >
                        {/* Close Area */}
                        <div
                            className="absolute inset-0 cursor-zoom-out"
                            onClick={handleClose}
                        />

                        <motion.div
                            className="relative z-10 max-w-4xl w-full px-4"
                            initial={{ scale: 0.8, opacity: 0 }}
                            animate={{ scale: 1, opacity: 1 }}
                            exit={{ scale: 0.8, opacity: 0 }}
                            transition={{ type: 'spring', stiffness: 260, damping: 20 }}
                        >
                            {/* Image */}
                            <div className="max-w-[90vw] max-h-[90vh] flex items-center justify-center overflow-auto">
                                <WebPImage
                                    src={certificatePaths[selectedIndex]}
                                    alt={`Сертификат ${selectedIndex + 1}`}
                                    className="w-full h-auto object-contain rounded-lg shadow-2xl max-h-full max-w-full"
                                />
                            </div>

                            {/* Close button */}
                            <button
                                className="absolute top-2 right-2 text-white/90 hover:text-white bg-black/40 hover:bg-black/60 rounded-full p-1"
                                onClick={handleClose}
                                aria-label="Закрыть"
                            >
                                <XMarkIcon className="w-6 h-6" />
                            </button>

                            {/* Prev / Next */}
                            <button
                                className="absolute left-2 top-1/2 -translate-y-1/2 text-white/90 hover:text-white bg-black/40 hover:bg-black/60 rounded-full p-1"
                                onClick={(e) => {
                                    e.stopPropagation();
                                    setSelectedIndex((selectedIndex - 1 + certificatePaths.length) % certificatePaths.length);
                                }}
                                aria-label="Предыдущее"
                            >
                                <ChevronLeftIcon className="w-6 h-6" />
                            </button>
                            <button
                                className="absolute right-2 top-1/2 -translate-y-1/2 text-white/90 hover:text-white bg-black/40 hover:bg-black/60 rounded-full p-1"
                                onClick={(e) => {
                                    e.stopPropagation();
                                    setSelectedIndex((selectedIndex + 1) % certificatePaths.length);
                                }}
                                aria-label="Следующее"
                            >
                                <ChevronRightIcon className="w-6 h-6" />
                            </button>
                        </motion.div>
                    </motion.div>
                )}
            </AnimatePresence>
        </div>
    );
};

export default Certificates; 