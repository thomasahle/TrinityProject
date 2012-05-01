
type Carriage = Int
type Train = [Carriage]
type LinearComponent = [Train] -> [Train]


idComponent :: LinearComponent
idComponent x = x;

chainComponent :: LinearComponent
chainComponent [] = []
chainComponent [x] = []
chainComponent (x:y:xs) = (x++y):chainComponent(xs)


flipComponent :: LinearComponent
flipComponent [] = []
flipComponent [x] = []
flipComponent (x:y:xs) = y:x:(flipComponent xs)

tailComponent :: LinearComponent
tailComponent xs = tail xs;


forkComponent :: [Train] -> ([Train],[Train]) --fork component returns the two lists of trains, first (UP) and second (DOWN)
forkComponent [] = ([],[]) 
forkComponent [x] = ([x],[])
forkComponent (x:y:zs) = let (zl,zr) = forkComponent(zs) in (x:zl,y:zr)

mergeComponent :: ([Train],[Train]) -> [Train] --merge component takes in two lists of trains, first (UP) and second (DOWN)
mergeComponent ([], ys) = ys
mergeComponent ((x:xs),ys) = x:mergeComponent(ys,xs)

combineComponents:: [LinearComponent] -> LinearComponent
combineComponents xs = foldr (.) idComponent (reverse xs)

funToPair :: (a->b, c->d) ->(a,c) -> (b,d)
funToPair (f,g)(a,b) = (f a,g b);

flipPair :: (a,b) -> (b,a)
flipPair (x,y) = (y,x)

(//) :: [LinearComponent] -> [LinearComponent] -> LinearComponent -- takes in two lists of components, top first then bottom second.
(//) tcs bcs = mergeComponent.flipPair.funToPair((combineComponents tcs),(combineComponents bcs)).forkComponent


(/\) :: [LinearComponent] -> [LinearComponent] -> LinearComponent -- takes in two lists of components, top first then bottom second.
(/\) tcs bcs = mergeComponent.funToPair((combineComponents tcs),(combineComponents bcs)).forkComponent
