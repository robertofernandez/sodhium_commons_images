package ar.com.sodhium.commons.img.areas;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class AnalizableSpace {
    private RectangularImageZone parent;
    private HashMap<String, RectangularImageZone> analizableRectangles;
    private HashMap<String, RectangularImageZone> nonAnalizableRectangles;
    private HashMap<String, RectangularZonesBlock> blocks;
    private ArrayList<RectangularZonesBlock> orderedBlocks;
    private HashMap<String, RectangularZonesBlock> clusteredBlocks;

    public AnalizableSpace(RectangularImageZone parent) {
        this.parent = parent;
        analizableRectangles = new HashMap<>();
        nonAnalizableRectangles = new HashMap<>();
        blocks = new HashMap<>();
        clusteredBlocks = new HashMap<>();
        orderedBlocks = new ArrayList<>();
    }

    public RectangularImageZone getParent() {
        return parent;
    }

    public static ArrayList<AnalizableSpace> createSpaces(RectangularImageZone base) {
        ArrayList<AnalizableSpace> output = new ArrayList<>();
        AnalizableSpace baseSpace = new AnalizableSpace(base);
        for (RectangularImageZone containedBlock : base.getChildren()) {
            if (containedBlock.getChildren().isEmpty()) {
                baseSpace.getAnalizableRectangles().put(containedBlock.getId(), containedBlock);
            } else {
                baseSpace.getNonAnalizableRectangles().put(containedBlock.getId(), containedBlock);
                output.addAll(createSpaces(containedBlock));
            }
        }
        output.add(baseSpace);
        return output;
    }

    public HashMap<String, RectangularImageZone> getAnalizableRectangles() {
        return analizableRectangles;
    }

    public HashMap<String, RectangularImageZone> getNonAnalizableRectangles() {
        return nonAnalizableRectangles;
    }

    public void createBlocks() {
        for (RectangularImageZone rectangle : analizableRectangles.values()) {
            boolean added = false;
            for (RectangularZonesBlock line : blocks.values()) {
                if (line.overlapsY(rectangle)) {
                    added = true;
                    line.addComponent(rectangle);
                    break;
                }
            }
            if (!added) {
                RectangularZonesBlock zonesBlock = new RectangularZonesBlock(rectangle);
                blocks.put(zonesBlock.getId(), zonesBlock);
            }
        }
        for (RectangularZonesBlock block : blocks.values()) {
            orderedBlocks.add(block);
        }
        for (int i = 0; i < orderedBlocks.size();) {
            if (orderedBlocksFusionAttempt(i)) {
                continue;
            } else {
                i++;
            }
        }
        blocks = new HashMap<>();
        for (RectangularZonesBlock block : orderedBlocks) {
            blocks.put(block.getId(), block);
        }
    }

    private boolean orderedBlocksFusionAttempt(int index) {
        if (index >= orderedBlocks.size() - 1) {
            return false;
        }
        boolean fused = false;
        ArrayList<RectangularZonesBlock> newSet = new ArrayList<>();
        for (int i = 0; i < index; i++) {
            newSet.add(orderedBlocks.get(i));
        }
        RectangularZonesBlock block = orderedBlocks.get(index);
        newSet.add(block);
        for (int i = index + 1; i < orderedBlocks.size(); i++) {
            RectangularZonesBlock anotherCluster = orderedBlocks.get(i);
            if (!fused) {
                if (block.overlaps(anotherCluster)) {
                    block.merge(anotherCluster);
                    fused = true;
                } else {
                    newSet.add(anotherCluster);
                }
            } else {
                newSet.add(anotherCluster);
            }
        }
        orderedBlocks = newSet;
        return fused;
    }

    public void clusterizeBlocks() {
        clusteredBlocks = new HashMap<>();
        for (RectangularZonesBlock baseBlock : blocks.values()) {
            baseBlock.getComponents().sort(new Comparator<RectangularImageZone>() {
                @Override
                public int compare(RectangularImageZone rectangle, RectangularImageZone anotherRectangle) {
                    return (Integer.valueOf(rectangle.getX())).compareTo(Integer.valueOf(anotherRectangle.getX()));
                }
            });
            RectangularZonesBlock currentBlock = null;
            for (RectangularImageZone rectangle : baseBlock.getComponents()) {
                if (currentBlock == null) {
                    currentBlock = new RectangularZonesBlock(rectangle);
                    currentBlock.setParent(parent);
                    parent.getChildrenBlocks().put(currentBlock.getId(), currentBlock);
                    clusteredBlocks.put(currentBlock.getId(), currentBlock);
                } else {
                    RectangularZonesBlock proposeAdd = currentBlock.proposeAdd(rectangle);
                    if (!overlapsNonAnalizableRectangles(proposeAdd)) {
                        currentBlock.addComponent(rectangle);
                    } else {
                        currentBlock = new RectangularZonesBlock(rectangle);
                        currentBlock.setParent(parent);
                        parent.getChildrenBlocks().put(currentBlock.getId(), currentBlock);
                        clusteredBlocks.put(currentBlock.getId(), currentBlock);
                    }
                }
            }
        }
    }

    private boolean overlapsNonAnalizableRectangles(RectangularZonesBlock proposeAdd) {
        for (RectangularImageZone nonAnalizableRectangle : nonAnalizableRectangles.values()) {
            if (proposeAdd.overlaps(nonAnalizableRectangle)) {
                return true;
            }
        }
        return false;
    }

    public HashMap<String, RectangularZonesBlock> getBlocks() {
        return blocks;
    }

    public HashMap<String, RectangularZonesBlock> getClusteredBlocks() {
        return clusteredBlocks;
    }

    @Override
    public String toString() {
        return parent.toString();
    }
}
