# Steps in a Simulation cycle

1. Clear the graphic scene
2. Process inputs
3. Process Camera logic
4. Every creature gets its current internal state updated by the environment (To prep for neural simulation)
5. Process logic for every model object. Creatures will perform neural simulation. At the end of this, every creature will have updated its future state while processing neurons.
6. Switch out creatures' current internal state with its future state
7. Physics simulations for one timestep are run
