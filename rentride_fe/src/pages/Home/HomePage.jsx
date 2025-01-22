import React from "react";
import HeroSection from "../../components/HeroSection/HeroSection";
import Brands from "../../components/Brands/Brands";
import Features from "../../components/Features/Features";

export default function HomePage() {
  return (
    <div className="home-page">
      <HeroSection />
      <Features />
      <Brands />
    </div>
  );
}
