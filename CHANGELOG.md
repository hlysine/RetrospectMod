# Changelog

## [0.9.1] - UI Update - 2022-09-13

### Power

- Change Timer power description

### UI

- Add details in timeline FTUE
- Improve "Cards played this turn" button VFX

## [0.9.0] - Art & Relics Update - 2022-09-12

### Mechanics

- Fix timeline positioning when new monsters are spawned mid-combat
- Reduce base timeline HP cost to 30% of current HP

### Cards

- Add 2 damage to Singularity
- Buff Fist Fight damage from 15(19) to 17(21)
- Buff Blind Strike damage from 12(16) to 15(19)
- Buff Unceasing Strike damage from 20(28) to 24(32)
- Reduce Rewind cost from 2 to 1, add exhaust, which is removed on upgrade

### Relics

- Remove Adaptive Shield
- Change all relic flavor text
- Rename Slanted Mirror to Shattered Mirror
- Nerf Shattered Mirror block gain from 10 to 5
- Add new relics
  - Smart Collar
  - Ankle Weights

### Events

- Rewrite Event Horizon event
- Edit Future Gadgets event slightly
- Add Retrospect text to Sensory Stone event

### Art

- Add power icons
  - Dimensional Rift
  - Into The Void
  - Master Of Deception
  - Shadow Form
  - Speed Is Life
  - The Best Defense
  - Smart Collar
- Add relic icons
  - Shattered Mirror
  - Smart Collar
  - Atomic Clock
  - Ankle Weights
- Fix blurry relic outlines

## [0.8.3] - Polishing Update - 2022-09-08

### Mechanics

- Skip consecutive wait actions in the end turn sequence
- Rename "trigger timeline" to "advance timeline"
- Improve timeline positioning to reduce overlaps
- Move timeline cards in-bound when they are hovered

### Cards

- Rename Quick thinker to Quick Thinking
- Nerf Quick Thinking: add ethereal
- Buff Tactical Retreat block gain from 8(12) to 10(14)
- Change Wild Card upgrade to simply reduce cost from 2 to 1
- Change Wild Card type and color based on the modifiers applied
- Increase Soulless Demon cost from 1 to 2
- Rename All-Out Attack to Fist Fight
- Show upgrade preview for curse cards that are upgradable
- Add new card
  - Time Leap

### Run Mods

- Increase difficulty of In Time: only heal half of unblock damage dealt, but lose 1 HP every 600ms

### Events

- Fix energy modifier description in Blank Canvas event

### Powers

- Use Draw power instead of a custom Quick Thinking power

### Art

- Synchronized Reflex power now use the old icon of Quick Thinking power

### VFX

- Add end turn FX to Unceasing Strike and Unceasing Sprint

### Misc

- Updated mod description

## [0.8.2] - Hotfix - 2022-09-01

### Mechanics

- Fix a crash when creating timelines for characters with no static images

## [0.8.1] - Balance Update - 2022-09-01

### Mechanics

- Update all death checks to consider the death animation
- Fix timelines getting interrupted when there are no valid attack targets on screen

### Cards

- Change Concoction to give Vulnerable instead of Weak
- Nerf Tap The Future: Gain 2 energy, shuffle a Void(Empty) into draw pile
- Nerf Paradox: Adds 1 damage at the end of turn
- Rework Warm Up: Change rarity from uncommon to rare, upgrade reduces cost instead of increasing damage
- Nerf Shadow Form: Add 1 Dexterity loss
- Buff Smart Hammer from rare to uncommon
- Rework Exponential Blast to be an X cost card, reduce X cost effect by 1
- Rework Rewind: upgrade grants Retain but does not reduce cost
- Nerf Recursion Hell damage from 2(5) to 2(4)

### Powers

- Update Shadow Form power description to match the card

### Events

- Increase Blank Canvas event HP loss by 1
- Reduce probability of Dexterity/Strength modifiers in Blank Canvas event

### Misc

- Disable action manager logging when not in debug mode

## [0.8.0] - Balance Update - 2022-08-30

### Mechanics

- Random timeline picker no longer pick timelines with no cards
- Fix timelines healing HP by collapsing twice

### Cards

- Rework Double Time to trigger a timeline once(twice), then collapse it
- Reduce Double Time cost from 1(0) to 0
- Change Double Time from starter to common card
- Rework Warm Up: Deal 1(2) damage. Next turn, gain Dexterity equal to unblocked damage dealt. Exhaust.
- Increase Deja Vu cost from 1 to 2
- Fix Deja Vu not working for exhausted/purged copies
- Buff Unceasing Strike damage from 13(16) to 20(28)
- Buff Converge to draw 2 cards instead of 1 per timeline
- Increase Recursion Hell cost from 0 to 1
- Remove timeline HP cost reduction from Shadow Form
- Nerf Recollection card count from 2(3) to 1(2)
- Nerf Master Of Deception damage from 5(7) to 4(6)
- Change Overcore description to match other cards that give turn-based powers
- Buff Unceasing Sprint power amount from 1 to 2
- Increase Wild Card cost from 1 to 2
- Add new cards
  - Rewind (replaces Double Time in starter deck)
  - Exponential Blast

### Run Mods

- Fix In Time mod triggering effects related to losing HP from cards

### Events

- Blank Canvas event now has uneven weights for different card text choices

### Relics

- Add keywords to all relics

### Art

- Change the placeholder image of Charge Rifle

### VFX

- Add glow to Paradox when it is in effect

### Misc

- USe relic info classes provided by STSItemInfo

## [0.7.1] - Hotfix - 2022-08-26

### Art

- Use correct icon for Energized power granted by Quantum Hourglass

### Run Mods

- Fix In Time timer not stopping after save and quit
- Stop In Time timer instantly when the end turn button is pressed

## [0.7.0] - Run Mod Update - 2022-08-25

### Run Mods

- Add mod In Time

## [0.6.1] - Hotfix - 2022-08-24

### Cards

- Fix Divert, Overcore and Shadow Form not getting their attributes updated on upgrade

### Misc

- Slight change to the metrics reported by the Blank Canvas event

## [0.6.0] - Art and QoL Update - 2022-08-23

### Mechanics

- Add FTUE tip when a timeline is first constructed
- Don't use RNG when there is only one choice

### Cards

- Fix Chaotic Defense and Chaotic Offense not triggering timelines after being played by relics
- Prevent random curse draws from drawing Singularity

### Art

- Add power icons
  - Active Echo
  - Defective Echo
  - Death Totem
- Add character art
- Change theme color from purple to green
- Change energy panel to green
- change card background from blue/purple to green/purple

### VFX

- Add glow effect for cards in timelines
- Add special purge effect for cards in timelines
- Fix timeline corpse images turning black

### Misc

- Collect gameplay data for balancing
- Gather relic information in a JSON file for online preview

## [0.5.0] - Balance Update - 2022-08-09

### Cards

- Fix Déjà vu starting with 0 block
- Fix keywords in Electric Shock
- Change Paradox from delusional to ethereal
- Remove Harvest
- Nerf Enigma damage from 7(9) to 6(8)
- Reduce Self Correction cost from 2(1) to 1(0) and remove exhaust
- Buff Master Of Deception damage from 3(5) to 5(7)
- Buff Unceasing Strike damage from 10(13) to 13(16)
- Buff The Best Defense block gain from 0(5) to 4(7)
- Nerf Recursion Hell damage from 4(6) to 2(5)
- Reduce Concerted Blast cost from 3(2) to 2 and add retain when upgraded
- Fix Singularity not retaining after first round
- Rename Avert to Chaotic Offense and add paradoxical attribute
- Buff Tactical Retreat block gain from 7(11) to 8(12)
- Buff All-Out Attack damage from 12(16) to 14(18)
- Reduce Recursive Affinity cost from 3(2) to 2, add retain on upgrade
- Change Unceasing Strike rarity from rare to uncommon
- Nerf Shattering Burst damage count from 10(15) to 7(10)
- Add new cards
  - Wild Card
  - Speed Is Life
  - Unceasing Sprint
  - Chaotic Defense
  - Psyche Out

### Events

- Add Blank Canvas event to build and give Wild Card

### Art

- Change Time Loop power icon
- Add power icons
  - Shortcut Power
  - Warm Up Power
  - Evanescence Power

### VFX

- Speed up timeline construction effect when in fast mode
- Make the timeline aura effect change color immediately when a timeline is triggered

## [0.4.0] - Content Update - 2022-06-26

### Mechanics

- Fix a bug with timeline targeting that causes a card to target a collapsed timeline
- Fix ordering of cards replaying other cards

### Cards

- Rework Alternative Reality: Gain block when in timeline, draw cards otherwise
- Buff Energy Conversion to give X+1(X+2) Miracles
- Nerf Rewrite History to cost 3 energy
- Fix Overcore constructing too many timelines
- Rework Time Loop: Double(triple) a timeline's cards
- Change Lightspeed Leap, Fast Forward and Telekinesis to have common rarity
- Buff Evanescence block gain from 3(5) to 5(7)
- Nerf Sleight Of Hand damage from 8(12) to 6(9)
- Reformat multiple card descriptions
- Add new cards
  - Unceasing Strike
  - Recursive Affinity
  - Death Totem
  - Converge
  - Self Correction
  - Kinetic Energy
  - Shattering Burst
  - Fractal Blade
  - Anamnesis
  - Asset Procurement
  - Synchronized Reflex
  - Dimensional Rift
  - Vanishing Trick
  - Recursion Hell
  - Enigma
  - Concoction
  - Smart Hammer
  - Master Of Deception
  - Concerted Blast
  - Into The Void
  - Deviated Burst
  - The Best Defense

### Powers

- Change Timer Power description when there are no cards in the timeline
- Remove Antique Clock Power and Atomic Clock Power - their effects are now handled by the corresponding relics
- Add new powers
  - Death Totem Power (card power)
  - Synchronized Reflex Power (card power)
  - Dimensional Rift Power (card power)
  - Master Of Deception Power (card power)
  - Into The Void Power (card power)
  - The Best Defense Power (card power)

### Art

- Rework energy icons
- Change placeholder image of All Out Attack
- Add power icons
  - Energized Power
  - Timer Power
  - Timed Death Power
  - Frozen Power
  - Quick Thinking Power
  - Time Loop Power
- Add relic icons
  - Antique Clock
  - Quantum Hourglass
  - Bottled Singularity

### UI

- Hide timeline peek button when already peeking from a screen

### VFX

- Improve card replay VFX to avoid cards stacking together

### Misc

- Update dependency: FriendlyMonsters
- Integrate STSCardInfo to centralize card values

## [0.3.2] - Hotfix - 2022-06-12

### Mechanics

- Change timelines to create cards with the same UUID
- Change all global card actions to also affect cards in timelines

### VFX

- Fix incorrect delay in timeline construction VFX

## [0.3.1] - VFX & Balancing Update - 2022-06-12

### Mechanics

- Fix a bug where dead timelines can be triggered

### Cards

- Make Noxious Wraith exhaust and increase dexterity loss from 4(2) to 5(3)
- Fix non-upgraded description of Defective Echo

### VFX

- Add flying orb VFX when constructing a timeline
- Highlight the newly constructed timeline with a faint circle
- Change timeline aura color when it is being triggered and will play cards

## [0.3.0] - QoL & Content Update - 2022-06-10

### Mechanics

- Use `cardRandomRng` instead of `cardRng` when choosing a random timeline to not affect discovery
- Check `MAX_HAND_SIZE` instead of hard-coding 10 to support increased hand capacity from other mods
- Fix timelines being interrupted by an unplayable card
- Fix timeline interactions with Timer Eater: timelines are now interrupted by Time Warp power

### Character

- Change starting relic from Adaptive Shield to Antique Clock
- Buff starting HP from 70 to 77

### Cards

- Fix pluralization in cards
- Change Telekinesis to still trigger Thorns effect
- Change Adaptive Shield to an uncommon relic
- Rename Into The Void to Double Time
- Rework Double Time: always triggers a timeline once only, but costs 0 when upgraded
- Rework Overcore: always gives 1 Frozen, but is Exhaust if not upgraded
- Swap names of Misdirection and Avert
- Nerf Misdirection block gain from 10(15) to 8(12)
- Rework Transmute: always discard cards, discard 2 cards when upgraded
- Nerf Rewrite History damage from 2(3) to 1(2)
- Reduce Time Loop cost from 3 to 2(1)
- Rework Shortcut: reduce timeline HP cost by 5%(7%) instead of 8%, loses Innate
- Rework Evanescence: Whenever you draw a Status card, gain 3(5) block
- Nerf Defective Echo card count from 2(3) to 1(2)
- Fix a bug in Defective Echo that prevents you from using it in 2 consecutive rounds
- Buff Noxious Wraith Intangible gain from 1 to 2
- Change target of Divine Eye, Energy Conversion, Fast Forward, Hat Trick, Recollection, and Transmute from self to none
- Add new Curse card Singularity
- Add new Power card Quick Thinker
- Rework Shadow Form: Reduce timeline HP cost by 10%. All timeline cards construct +1 timeline. Ethereal. (Lose Ethereal
  when upgraded)

### Relics

- Rename Antique Stopwatch to Antique Clock
- Add new event relic Bottled Singularity
- Add new boss relic Atomic Clock

### Events

- Nerf Council of Ghosts event for the Retrospect: the Retrospect now only gets 3(2) Apparition cards
- Add Event Horizon event to give Bottled Singularity relic
- Change wordings in Mysterious Prophet event
- Add Future Gadgets event for non-Retrospect characters

### Potions

- Make Timeline Potion available for all characters
- Update Timeline Potion to be affected by Sacred Bark
- Add new uncommon potion Butterfly In A Jar

### UI

- Add a screen to view cards played this turn
- Add a screen to view cards in a timeline
- Add a peek button on timelines to bring up the card screen
- Fix a z-ordering issue of timeline cards in hand card select screen

### VFX

- Instead of disappearing instantly, play death animation when collapsing a timeline
- Fix Defect orbs not following the player when there are timelines
- Use a consistent card copying method in Timer power
- Remove card glow for timeline targeting cards when there are no timelines
- Add a special dialog for Time Eater when playing as the Retrospect

### Compatibility

- Fix a crash when dying as the Retrospect in Together In Spire

### Misc

- Migrate from Kobting/STSFriendlyMinions to hlysine/FriendlyMonsters for timelines

## [0.2.1] - Hotfix - 2022-05-23

### Mechanics

- Skip collapse timeline action if the timeline is already dead

### Cards

- Fix a bug that prevents Into The Void+ from triggering a timeline twice
- Change Temporal Anomaly to trigger timelines one after the other

### VFX

- Skip Rewrite History VFX if there are no cards in discard pile

## [0.2.0] - Second Technical Test - 2022-05-22

### Mechanics

- Timelines now play cards one after the other instead of queueing all cards at once
- Reword timeline descriptions and keywords
- Timelines can now hold and play timeline construction cards
- Add new card attribute Delusional
- Add new card attribute Paradoxical
- Add new card targeting: timeline targeting
- Allow base game characters to also have max 5 concurrent timelines
- Add Time Loop mechanic with proper VFX

### Cards

- Change Misdirection to play the attack card before constructing a timeline
- Change Tap The Future to shuffle 1 instead of 2 Empty cards
- Fix Rewrite History not exhausting
- Rework Shadow Form to increase card draw by a constant amount instead of scaling up per round
- Change Empty to be upgradable
- Add new status card Paradox
- Change Sleight Of Hand to be able to replay other Sleight Of Hand cards
- Add Paradoxical to Sleight Of Hand
- Rework Into The Void to target one timeline and no longer exhaust
- Add new cards:
  - Time Loop
  - Shortcut
  - Electric Shock
  - Avert
  - Tactical Retreat
  - Lightspeed Leap
  - Fast Forward
  - All-Out Attack
  - Warm Up
  - Telekinesis
  - Recollection
  - Ambush
  - Hat Trick
  - Blind Strike
  - Evanescence
  - Soulless Demon
  - Noxious Wraith
  - Defective Echo
  - Divine Eye

### Events

- Add Mysterious Prophet event

### Powers

- Powers that play cards at the end of a turn now play them sequentially, right before timelines do
- Frozen power now allows all auto-played cards to be played
- Add new powers:
  - Defective Echo (card power)
  - Energized (card power, same as base game power but use custom energy icon)
  - Evanescence (card power)
  - Shortcut (card power)
  - Time Loop (card power)
  - Warm Up (card power)

### VFX

- Change spire heart attack sequence slightly
- Add a VFX effect in Misdirection to show the chosen card before playing it
- Add death animation to timelines
- Change timelines to use the same models, animations and corpse images as the selected character
- Fixe incorrect z-order of hovered timeline cards
- Timeline cards now appear at the timeline before hovering above the timeline instead of appearing at the discard pile
- Hide timeline cards when the player is dead
- Add a VFX effect in Sleight Of Hand to show the chosen card before playing it
- Correct turn-based indicator for some powers
- Correct buff/debuff VFX for some powers

### Misc

- Update author field in code
- Update base game to version 03-07-2022
- Update dependencies

## [0.0.3] - Balancing Changes - 2022-04-08

### New content

- Add spire heart finisher sequence
- Add new relic: Slanted Mirror

### Changes

- Add additional safety checks in Alternative Reality
- Rename Breakneck Form to Shadow Form and reworked it
- Rework Rewrite History
- Timelines now cost 40% of your current HP
- Timeline construction cards can now be played even if the timeline cannot be constructed
- Update the list of authors
- Change the design of energy panel
- Change energy orb icons for cards
- Highlight the actual action in Alternative Reality
- Remove `_V` suffix from Strike and Defend
- Cards are no longer marked as seen by default
- Change Empty to be a colorless card
- Fix timeline placement when the player is surrounded
- Increase starting HP from 65 to 70
- Decrease maximum number of timelines from 10 to 5
- Set Divert to be the default card for Match and Keep event
- Set ascension HP loss to 4
- Remove 1 Strike and 1 Defend from starting deck, making the starting deck size now 10
- Rename Perpetual Hourglass to Quantum Hourglass and changed its flavor

## [0.0.2] - Hotfix - 2022-04-01

### Changed

- Update strings
- Prevent the use of timeline potion if a timeline cannot be created

## [0.0.1] - First Technical Test - 2022-03-31

### New content

- First technical test
  - Timeline mechanics
  - 21 cards
  - 1 potion
  - 3 relics
